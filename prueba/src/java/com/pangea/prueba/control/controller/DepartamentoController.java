package com.pangea.prueba.control.controller;

import com.pangea.prueba.control.modelo.entidad.Departamento;
import com.pangea.prueba.control.controller.util.JsfUtil;
import com.pangea.prueba.control.controller.util.PaginationHelper;
import com.pangea.prueba.control.servicio.RegistrosDepartamento.Fil;
import com.pangea.prueba.control.servicio.RegistrosDepartamento.Fil.Entry;
import com.pangea.prueba.control.servicio.Servicioweb_Service;
import com.pangea.prueba.modelo.bean.DepartamentoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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

@ManagedBean(name = "departamentoController")
@SessionScoped
public class DepartamentoController implements Serializable {
   @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.1.105_14070/PRUEBA/servicioweb.wsdl")
    private Servicioweb_Service service;

    private Departamento current;
    private DataModel items = null;
    @EJB
    private com.pangea.prueba.modelo.bean.DepartamentoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String buscar;
    private Departamento descripcion;
    private Departamento departamentoc;
    
    /** DECLARACION LAZY **/
    private List<Departamento> departamentos;
    private LazyDataModel<com.pangea.prueba.control.servicio.Departamento> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/

    public Departamento getCurrent() {
        return current;
    }

    public void setCurrent(Departamento current) {
        this.current = current;
    }
    
    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public Departamento getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Departamento descripcion) {
        this.descripcion = descripcion;
    }

    public void busqueda() {
        descripcion = getFacade().descripcionDepartamento(buscar);
      
    }
    
    public DepartamentoController() {
    }

    public Departamento getSelected() {
        if (current == null) {
            current = new Departamento();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DepartamentoFacade getFacade() {
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
        current = (Departamento) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Departamento();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            
            //Para Crear por Servicios Web
            com.pangea.prueba.control.servicio.Departamento insert=new com.pangea.prueba.control.servicio.Departamento();
            insert.setDepartamentoid(current.getDepartamentoid());
            insert.setNombre(current.getNombre());
            insert.setDescripcion(current.getDescripcion());
            this.crearDepartameto(insert);
            
            //Llamo al metodo para que recargue cuando creo uno nuevo
            inicializarLazy();
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DepartamentoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Departamento) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DepartamentoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Departamento) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DepartamentoDeleted"));
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

    @FacesConverter(forClass = Departamento.class)
    public static class DepartamentoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartamentoController controller = (DepartamentoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departamentoController");
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
            if (object instanceof Departamento) {
                Departamento o = (Departamento) object;
                return getStringKey(o.getDepartamentoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Departamento.class.getName());
            }
        }
    }
    
    
    /** INICIO LAZY**/
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.prueba.control.servicio.Departamento>() {

            @Override
            public List<com.pangea.prueba.control.servicio.Departamento> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
               int cantidad = listarDepartamento();
                
               if (cantidad > 0) {
                   pagIndex = first;
                   fields = filters;
                   sortF = sortField;
                   sortB = true;
                   String cadena = "";
                   lazyModel.setWrappedData(null);
                   cantidad = listarDepartamento();
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
                   
                   return registrosDepartamento(sortF, sortB, lis, algo, cadena);

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

    public LazyDataModel<com.pangea.prueba.control.servicio.Departamento> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.prueba.control.servicio.Departamento> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public void recargarLazyModel() {
       
        lazyModel.setWrappedData(lazyModel.load(pagIndex, lazyModel.getPageSize(), sortF,SortOrder.ASCENDING, fields));
    }
//FIN LAZY
    
    public void onEdit(RowEditEvent event) {
        departamentoc=(Departamento) event.getObject();
        getFacade().edit(departamentoc);
        System.out.println("Edito");
    }  
      
    public void onCancel(RowEditEvent event) {
        System.out.println("Cancelo");
    }
    
    public void eliminarCargo(){
        Departamento eliminar=getFacade().find(current.getDepartamentoid());
        if(eliminar.getEmpleadoList().isEmpty()){
            getFacade().remove(eliminar);
            mostrarMensaje(0, "Advertencia", "Se Elimino");
        }
        else{
            mostrarMensaje(0, "Advertencia", "NO se Elimino");
        }
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

   private void crearDepartameto(com.pangea.prueba.control.servicio.Departamento registroDepartamento) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        port.crearDepartameto(registroDepartamento);
    }

    private java.util.List<com.pangea.prueba.control.servicio.Departamento> registrosDepartamento(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.prueba.control.servicio.RegistrosDepartamento.Fil fil, java.lang.String cad) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.registrosDepartamento(sortF, sortB, range, fil, cad);
    }

    private int listarDepartamento() {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.listarDepartamento();
    }
}