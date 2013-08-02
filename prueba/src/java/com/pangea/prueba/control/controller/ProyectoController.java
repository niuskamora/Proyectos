package com.pangea.prueba.control.controller;

import com.pangea.prueba.control.modelo.entidad.Proyecto;
import com.pangea.prueba.control.controller.util.JsfUtil;
import com.pangea.prueba.control.controller.util.PaginationHelper;
import com.pangea.prueba.control.modelo.entidad.Empleado;
import com.pangea.prueba.control.servicio.RegistrosProyecto.Fil;
import com.pangea.prueba.control.servicio.RegistrosProyecto.Fil.Entry;
import com.pangea.prueba.control.servicio.Servicioweb_Service;
import com.pangea.prueba.modelo.bean.ProyectoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "proyectoController")
@SessionScoped
public class ProyectoController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.1.105_14070/PRUEBA/servicioweb.wsdl")
    private Servicioweb_Service service;

    private Proyecto current;
    private DataModel items = null;
    @EJB
    private com.pangea.prueba.modelo.bean.ProyectoFacade ejbFacade;
    @EJB
    private com.pangea.prueba.modelo.bean.EmpleadoFacade ejbempleadoFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Empleado> empleados;
    private Empleado empleadoseleccionado;
    private String buscar;
    private Proyecto nombre;
    private Proyecto proyectoc;
    
    /** DECLARACION LAZY **/
    private List<Proyecto> proyectos;
    private LazyDataModel<com.pangea.prueba.control.servicio.Proyecto> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/

    
    public Proyecto getCurrent() {
        return current;
    }

    public void setCurrent(Proyecto current) {
        this.current = current;
    }
    
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public Empleado getEmpleadoseleccionado() {
        return empleadoseleccionado;
    }

    public void setEmpleadoseleccionado(Empleado empleadoseleccionado) {
        this.empleadoseleccionado = empleadoseleccionado;
    }
    
    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public Proyecto getNombre() {
        return nombre;
    }

    public void setNombre(Proyecto nombre) {
        this.nombre = nombre;
    }

    public void busqueda() {
        nombre = getFacade().todoProyecto(buscar);
     
    }
    
    public Proyecto getProyectoc() {
        return proyectoc;
    }

    public void setProyectoc(Proyecto proyectoc) {
        this.proyectoc = proyectoc;
    }
    
    
    public ProyectoController() {
    }
    
    @PostConstruct
    public void iniciar(){
        empleados=ejbempleadoFacade.findAll();
    }

    public Proyecto getSelected() {
        if (current == null) {
            current = new Proyecto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProyectoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Proyecto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Proyecto();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setEmpleadoid(empleadoseleccionado);
            
            //Para Crear por Servicios Web
            
            com.pangea.prueba.control.servicio.Proyecto insert=new com.pangea.prueba.control.servicio.Proyecto();
            com.pangea.prueba.control.servicio.Empleado insert2=new com.pangea.prueba.control.servicio.Empleado();
            
            insert.setProyectoid(current.getProyectoid());
            insert.setNombre(current.getNombre());
            insert.setDescripcion(current.getDescripcion());
            insert2.setEmpleadoid(current.getEmpleadoid().getEmpleadoid());
            insert.setEmpleadoid(insert2);
            this.crearProyecto(insert);
            
            //Llamo al metodo para que recargue cuando creo uno nuevo
            inicializarLazy();
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Proyecto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Proyecto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProyectoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Proyecto.class)
    public static class ProyectoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProyectoController controller = (ProyectoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proyectoController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Proyecto) {
                Proyecto o = (Proyecto) object;
                return getStringKey(o.getProyectoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Proyecto.class.getName());
            }
        }
    }
    
    @FacesConverter(forClass = Empleado.class)
    public static class EmpleadoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProyectoController controller = (ProyectoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "proyectoController");
            return controller.ejbempleadoFacade.find(getKey(value));
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(java.math.BigDecimal value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Empleado) {
                Empleado o = (Empleado) object;
                return getStringKey(o.getEmpleadoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Empleado.class.getName());
            }
        }
    }
    
     /** INICIO LAZY**/
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.prueba.control.servicio.Proyecto>() {

            @Override
            public List<com.pangea.prueba.control.servicio.Proyecto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
                int cantidad = listarProyecto();
                
               if (cantidad > 0) {
                   pagIndex = first;
                   fields = filters;
                   sortF = sortField;
                   sortB = true;
                   String cadena = "";
                   lazyModel.setWrappedData(null);
                   cantidad = listarProyecto();
                   lazyModel.setRowCount(cantidad);
                   List<Integer> lis= new ArrayList();
                   lis.add(first);
                   lis.add(first+pageSize);
                   Fil algo = new Fil();
                   for (Map.Entry e : filters.entrySet()) {
                       Entry otro=new Entry();
                       
                       otro.setKey(e.getKey().toString());
                       otro.setValue(e.getValue().toString());
                       algo.getEntry().add(otro);
                   }   
                   
                   return registrosProyecto(sortF, sortB, lis, algo, cadena);

               }
               return null;
            }
        };

        lazyModel.setRowCount((int) getFacade().count());
        //lazyModel.setRowCount(10);

        if (lazyModel.getPageSize() == 0) {
            lazyModel.setPageSize(1);
        }
        if (lazyModel.getRowCount() == 0) {
            lazyModel.setRowCount(10);
        }

    }

    public LazyDataModel<com.pangea.prueba.control.servicio.Proyecto> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.prueba.control.servicio.Proyecto> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public void recargarLazyModel() {
       
        lazyModel.setWrappedData(lazyModel.load(pagIndex, lazyModel.getPageSize(), sortF,SortOrder.ASCENDING, fields));
    }
//FIN LAZY
    
    public void onEdit(RowEditEvent event) {
        proyectoc=(Proyecto) event.getObject();
        getFacade().edit(proyectoc);
        System.out.println("Edito");
    }  
      
    public void onCancel(RowEditEvent event) {
        System.out.println("Cancelo");
    } 
    
    public void eliminarProyecto(){
        Proyecto eliminar=getFacade().find(current.getProyectoid());
        
            getFacade().remove(eliminar);
            mostrarMensaje(0, "Advertencia", "Se Elimino");
        
        //Llamo al metodo para que recargue cuando elimino
        inicializarLazy();
        
    }
    
    public void mostrarMensaje(int _opcMensaje, String _cabeceraMensaje, String _cuerpomensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        switch (_opcMensaje) {
            case 0: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 1: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 2: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 3: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
        }
    }

    private void crearProyecto(com.pangea.prueba.control.servicio.Proyecto registroProyecto) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        port.crearProyecto(registroProyecto);
    }

    private java.util.List<com.pangea.prueba.control.servicio.Proyecto> registrosProyecto(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.prueba.control.servicio.RegistrosProyecto.Fil fil, java.lang.String cad) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.registrosProyecto(sortF, sortB, range, fil, cad);
    }

    private int listarProyecto() {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.listarProyecto();
    }
}
