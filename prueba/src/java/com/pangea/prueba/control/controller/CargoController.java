package com.pangea.prueba.control.controller;

import com.pangea.prueba.control.modelo.entidad.Cargo;
import com.pangea.prueba.control.controller.util.JsfUtil;
import com.pangea.prueba.control.controller.util.PaginationHelper;
import com.pangea.prueba.control.servicio.Servicioweb_Service;
import com.pangea.prueba.modelo.bean.CargoFacade;
import com.pangea.prueba.control.servicio.Registroscargo.Fil;
import com.pangea.prueba.control.servicio.Registroscargo.Fil.Entry;
import com.pangea.prueba.control.servicios.Practicaservicio_Service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.omg.CORBA.Current;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;

@ManagedBean(name = "cargoController")
@SessionScoped

public class CargoController implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.1.101_15362/aplicacion/practicaservicio.wsdl")
    private Practicaservicio_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.1.105_14070/PRUEBA/servicioweb.wsdl")
    private Servicioweb_Service service;

    private Cargo current;
    private DataModel items = null;
    @EJB
    private com.pangea.prueba.modelo.bean.CargoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String nombre, apellido;
    private Date date1;
    private int seleccion;
    private DualListModel<String> ciudad;
    private String value;
    private TreeNode root;
    private BubbleChartModel bubbleModel;
    private List<String> images;
    private List<String> selectedMovies;  
    private List<String> selectedOptions;  
    private Map<String,String> movies;
    private String buscar;
    private Cargo descripcion;
    private Cargo cargoc;
    
    /** DECLARACION LAZY **/
    private List<Cargo> cargos;
    private LazyDataModel<com.pangea.prueba.control.servicio.Cargo> lazyModel;
    private int pagIndex = 0;
    private Map<String, String> fields = new HashMap<String, String>();
    private String sortF = null;
    private boolean sortB = false;
    private int paginacion = 10;
    /** FIN LAZY **/

    
     public Cargo getCurrent() {
        return current;
    }

    public void setCurrent(Cargo current) {
        this.current = current;
    }
    
    public Cargo getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Cargo descripcion) {
        this.descripcion = descripcion;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }
    
    public void busqueda() {
        descripcion = getFacade().descripcionCargo(buscar);
      
    }
    
    public List<String> getSelectedMovies() {  
        return selectedMovies;  
    }  
    public void setSelectedMovies(List<String> selectedMovies) {  
        this.selectedMovies = selectedMovies;  
    }  
  
    public List<String> getSelectedOptions() {  
        return selectedOptions;  
    }  
    public void setSelectedOptions(List<String> selectedOptions) {  
        this.selectedOptions = selectedOptions;  
    }  
  
    public Map<String, String> getMovies() {  
        return movies;  
    }
    
    public List<String> getImages() {  
        return images;  
    }
    
    public BubbleChartModel getBubbleModel() {
        return bubbleModel;
    }

    public void setBubbleModel(BubbleChartModel bubbleModel) {
        this.bubbleModel = bubbleModel;
    }
        
    private void createBubbleModel() {  
        bubbleModel = new BubbleChartModel();  
  
        bubbleModel.add(new BubbleChartSeries("Acura", 70, 183,55));  
        bubbleModel.add(new BubbleChartSeries("Alfa Romeo", 45, 92, 36));  
        bubbleModel.add(new BubbleChartSeries("AM General", 24, 104, 40));  
        bubbleModel.add(new BubbleChartSeries("Bugatti", 50, 123, 60));  
        bubbleModel.add(new BubbleChartSeries("BMW", 15, 89, 25));  
        bubbleModel.add(new BubbleChartSeries("Audi", 40, 180, 80));  
        bubbleModel.add(new BubbleChartSeries("Aston Martin", 70, 70, 48));  
    }  
    
    public void handleToggle(ToggleEvent event) {  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fieldset Toggled", "Visibility:" + event.getVisibility());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    } 
    
    public TreeNode getRoot() {  
        return root;  
    } 
    
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }  

    public DualListModel<String> getCiudad() {
        return ciudad;
    }

    public void setCiudad(DualListModel<String> ciudad) {
        this.ciudad = ciudad;
    }

    public int getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(int seleccion) {
        this.seleccion = seleccion;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public CargoController() {
        createBubbleModel();
        
        nombre="Niuska";
        apellido="Mora";
        
        //Declara e instancia Ciudades para el ejmplo de la lista dual basica
        List<String> ciudadSource = new ArrayList<String>();  
        List<String> ciudadTarget = new ArrayList<String>();  
          
        ciudadSource.add("San Cristóbal");  
        ciudadSource.add("Caracas");  
        ciudadSource.add("Merida");  
        ciudadSource.add("Barquisimeto");  
        ciudadSource.add("Maracaibo");  
          
        ciudad = new DualListModel<String>(ciudadSource, ciudadTarget);
        
        //Declara e instancia los valores del tree-horizontal
        root = new DefaultTreeNode("Principal", null);  
        TreeNode node0 = new DefaultTreeNode("Número 0", root);  
        TreeNode node1 = new DefaultTreeNode("Número 1", root);  
        TreeNode node2 = new DefaultTreeNode("Número 2", root);  
          
        TreeNode node00 = new DefaultTreeNode("Número 0.0", node0);  
        TreeNode node01 = new DefaultTreeNode("Número 0.1", node0);  
          
        TreeNode node10 = new DefaultTreeNode("Número 1.0", node1);  
        TreeNode node11 = new DefaultTreeNode("Número 1.1", node1);  
          
        TreeNode node000 = new DefaultTreeNode("Número 0.0.0", node00);  
        TreeNode node001 = new DefaultTreeNode("Número 0.0.1", node00);  
        TreeNode node010 = new DefaultTreeNode("Número 0.1.0", node01);  
          
        TreeNode node100 = new DefaultTreeNode("Número 1.0.0", node10);
        
        //Declara e instancia las imagenes a mostrar
        images = new ArrayList<String>();  
        images.add("1.jpg");  
        images.add("2.jpg");  
        images.add("3.jpg");  
        images.add("4.jpg");
        images.add("5.jpg");  
        images.add("6.jpg");  
        images.add("7.jpg");  
        images.add("8.jpg");
        images.add("9.jpg");  
        images.add("10.jpg");
        
        //Declara e instancia las opciones
        movies = new HashMap<String, String>();  
        movies.put("Niuska", "Niuska");  
        movies.put("José", "José");  
        movies.put("Mariela", "Mariela");  
        movies.put("Gerson", "Gerson");
     
    }
    
    
    
    public Cargo getSelected() {
        if (current == null) {
            current = new Cargo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CargoFacade getFacade() {
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
        current = (Cargo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cargo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            
            //Para Crear por Servicios Web
            com.pangea.prueba.control.servicio.Cargo insert=new com.pangea.prueba.control.servicio.Cargo();
            insert.setCargoid(current.getCargoid());
            insert.setNombre(current.getNombre());
            insert.setDescripcion(current.getDescripcion());
            this.crearCargo(insert);
            
            //Llamo al metodo para que recargue cuando creo uno nuevo
            inicializarLazy();
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CargoCreated"));
             return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Cargo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CargoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cargo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CargoDeleted"));
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

    @FacesConverter(forClass = Cargo.class)
    public static class CargoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CargoController controller = (CargoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cargoController");
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
            if (object instanceof Cargo) {
                Cargo o = (Cargo) object;
                return getStringKey(o.getCargoid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cargo.class.getName());
            }
        }
    }
    
    
    /** INICIO LAZY**/
    public void inicializarLazy() {
        lazyModel = new LazyDataModel<com.pangea.prueba.control.servicio.Cargo>() {

            @Override
            public List<com.pangea.prueba.control.servicio.Cargo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                paginacion = pageSize;
                int cantidad = listarCargo();
                
               if (cantidad > 0) {
                   pagIndex = first;
                   fields = filters;
                   sortF = sortField;
                   sortB = true;
                   String cadena = "";
                   lazyModel.setWrappedData(null);
                   cantidad = listarCargo();
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
                   
                   return registroscargo(sortF, sortB, lis, algo, cadena);

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

    public LazyDataModel<com.pangea.prueba.control.servicio.Cargo> getLazyModel() {
        if (lazyModel == null) {
            inicializarLazy();
        }
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<com.pangea.prueba.control.servicio.Cargo> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public void recargarLazyModel() {
       
        lazyModel.setWrappedData(lazyModel.load(pagIndex, lazyModel.getPageSize(), sortF,SortOrder.ASCENDING, fields));
    }
//FIN LAZY
    
    public void onEdit(RowEditEvent event) {
        cargoc=(Cargo) event.getObject();
        getFacade().edit(cargoc);
        System.out.println("Edito");
    }  
      
    public void onCancel(RowEditEvent event) {
        System.out.println("Cancelo");
    } 
    
    public void eliminarCargo(){
        Cargo eliminar=getFacade().find(current.getCargoid());
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

    private void crearCargo(com.pangea.prueba.control.servicio.Cargo registroCargo) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        port.crearCargo(registroCargo);
    }

    private int listarCargo() {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.listarCargo();
    }

    private java.util.List<com.pangea.prueba.control.servicio.Cargo> registroscargo(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.prueba.control.servicio.Registroscargo.Fil fil, java.lang.String cad) {
        com.pangea.prueba.control.servicio.Servicioweb port = service.getServiciowebPort();
        return port.registroscargo(sortF, sortB, range, fil, cad);
    }

    private int contarcargo() {
        com.pangea.prueba.control.servicios.Practicaservicio port = service_1.getPracticaservicioPort();
        return port.contarcargo();
    }

    private java.util.List<com.pangea.prueba.control.servicios.Cargo> listacargo(java.lang.String sortF, boolean sortB, java.util.List<java.lang.Integer> range, com.pangea.prueba.control.servicios.Listacargo.Fil fil, java.lang.String cad) {
        com.pangea.prueba.control.servicios.Practicaservicio port = service_1.getPracticaservicioPort();
        return port.listacargo(sortF, sortB, range, fil, cad);
    }

   

    

}