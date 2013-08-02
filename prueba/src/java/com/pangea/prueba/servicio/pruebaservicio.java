/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.servicio;

import com.pangea.prueba.control.modelo.entidad.Cargo;
import com.pangea.prueba.control.modelo.entidad.Departamento;
import com.pangea.prueba.control.modelo.entidad.Empleado;
import com.pangea.prueba.control.modelo.entidad.Proyecto;
import com.pangea.prueba.modelo.bean.CargoFacade;
import com.pangea.prueba.modelo.bean.DepartamentoFacade;
import com.pangea.prueba.modelo.bean.EmpleadoFacade;
import com.pangea.prueba.modelo.bean.ProyectoFacade;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.primefaces.model.SortOrder;

/**
 *
 * @author PANGEA
 */
@WebService(serviceName = "pruebaservicio")
public class pruebaservicio {

     @EJB
    DepartamentoFacade departamentoFacade;
     
     @EJB
    CargoFacade cargoFacade;
     
     @EJB
    EmpleadoFacade empleadoFacade;
     
     @EJB
    ProyectoFacade proyectoFacade;
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
     
    @WebMethod(operationName = "listaDepartamento")
    public List<Departamento> listaDepartamento() {
    
        return departamentoFacade.retornaDepartamentos();
    }
    
    @WebMethod(operationName = "descripcionCargo")
    public Cargo descripcionCargo(@WebParam(name = "name") String txt) {
        
        return cargoFacade.descripcionCargo(txt);
        
    }
    
    @WebMethod(operationName = "listaDepartamentoMayor")
    public List<Departamento> listaDepartamentoMayor() {
    
        return departamentoFacade.listaDepartamentoMayorASeis();
    }
    
    @WebMethod(operationName = "promedioSueldo")
    public List<String[]> promedioSueldo() {
    
        return departamentoFacade.promedioSueldoSql();
    }
    
    
    
    @WebMethod(operationName = "cantidadEmpleados")
    public List<String[]> cantidadEmpleados() {
    
        return departamentoFacade.cantidadEmpleados();
    }
    
    @WebMethod(operationName = "cantidadEmpleadosJPQL")
    public List<String[]> cantidadEmpleadosJPQL() {
    
        return departamentoFacade.cantidadEmpleadosJPQL();
    }
    
    @WebMethod(operationName = "promedioSueldoGeneral")
    public BigDecimal promedioSueldoGeneral() {
    
        return empleadoFacade.promedioSueldoGeneral();
    }
    
    
    
    @WebMethod(operationName = "todo")
    public List<String[]> todo() {
    
        return empleadoFacade.mostrarEmpleadoDepartamentoCargo();
    }
    
    @WebMethod(operationName = "todoJPQL")
    public List<String[]> todoJPQL() {
    
        return empleadoFacade.mostrarEmpleadoDepartamentoCargoJPQL();
    }
    
    //Metodos para CREAR!!!
    
    @WebMethod (operationName = "crearCargo")
    public void crearCargo(@WebParam(name = "registroCargo") Cargo registroc){
       cargoFacade.insertarCargo(registroc);
    }
    
    @WebMethod (operationName = "crearDepartameto")
    public void crearDepartameto(@WebParam(name = "registroDepartamento") Departamento registrod){
       departamentoFacade.insertarDepartamento(registrod);
    }
    
    @WebMethod (operationName = "crearEmpleado")
    public void crearEmpleado(@WebParam(name = "registroEmpleado") Empleado registroe){
       empleadoFacade.insertarEmpleado(registroe);
    }
    
    @WebMethod (operationName = "crearProyecto")
    public void crearProyecto(@WebParam(name = "registroProyecto") Proyecto registrop){
       proyectoFacade.insertarProyecto(registrop);
    }
    
    //Metodos para contar y actualizar los valores de las tablas
    
    @WebMethod (operationName = "listarCargo")
    public int listarCargo(){
        return cargoFacade.count();
    }
    
   @WebMethod (operationName = "encontrarCargo")
   public List<Cargo> encontrarCargo(@WebParam(name = "sortF") String sortF,@WebParam(name = "sortB") boolean sortB, @WebParam(name = "range")  List<Integer> range, @WebParam(name = "fil") Map<String, String> filters,@WebParam(name = "cad") String cadena ) {
       
       int vector[]= new int[2];
       vector[0]=range.get(0);
       vector[1]=range.get(1);
       
       return cargoFacade.findRange(sortF, sortB, vector, filters, cadena);
   }
    
    @WebMethod (operationName = "listarDepartamento")
    public int listarDepartamento(){
        return departamentoFacade.count();
    }
    
    @WebMethod (operationName = "encontrarDepartamento")
    public List<Departamento> encontrarDepartamento(@WebParam(name = "sortF") String sortF,@WebParam(name = "sortB") boolean sortB, @WebParam(name = "range")  List<Integer> range, @WebParam(name = "fil") Map<String, String> filters,@WebParam(name = "cad") String cadena ) {
        
        int vector[]= new int[2];
        vector[0]=range.get(0);
        vector[1]=range.get(1);
        
        return departamentoFacade.findRange(sortF, sortB, vector, filters, cadena);
    }
    
    @WebMethod (operationName = "listarEmpleado")
    public int listarEmpleado(){
        return empleadoFacade.count();
    }
    
    @WebMethod (operationName = "encontrarEmpleado")
    public List<Empleado> encontrarEmpleado(@WebParam(name = "sortF") String sortF,@WebParam(name = "sortB") boolean sortB, @WebParam(name = "range")  List<Integer> range, @WebParam(name = "fil") Map<String, String> filters,@WebParam(name = "cad") String cadena ) {
        
        int vector[]= new int[2];
        vector[0]=range.get(0);
        vector[1]=range.get(1);
        
        return empleadoFacade.findRange(sortF, sortB, vector, filters, cadena);
    }    
    
    @WebMethod (operationName = "listarProyecto")
    public int listarProyecto(){
        return proyectoFacade.count();
    }
    
    @WebMethod (operationName = "encontrarProyecto")
    public List<Proyecto> encontrarProeycto(@WebParam(name = "sortF") String sortF,@WebParam(name = "sortB") boolean sortB, @WebParam(name = "range") List<Integer> range, @WebParam(name = "fil") Map<String, String> filters,@WebParam(name = "cad") String cadena ) {
        
        int vector[]= new int[2];
        vector[0]=range.get(0);
        vector[1]=range.get(1);
        
        return proyectoFacade.findRange(sortF, sortB, vector, filters, cadena);
    }
}
