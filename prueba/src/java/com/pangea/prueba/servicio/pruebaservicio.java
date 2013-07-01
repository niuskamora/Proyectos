/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.servicio;

import com.pangea.prueba.control.modelo.entidad.Cargo;
import com.pangea.prueba.control.modelo.entidad.Departamento;
import com.pangea.prueba.modelo.bean.CargoFacade;
import com.pangea.prueba.modelo.bean.DepartamentoFacade;
import com.pangea.prueba.modelo.bean.EmpleadoFacade;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

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
    
    @WebMethod(operationName = "promedioSueldoJPQL")
    public List<String[]> promedioSueldoJPQL() {
    
        return departamentoFacade.promedioSueldo();
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
    
    @WebMethod(operationName = "promedioSueldoGeneralJPQL")
    public double promedioSueldoGeneralJPQL() {
    
        return empleadoFacade.promedioSueldoGeneralJPQL();
    }
    
    @WebMethod(operationName = "todo")
    public List<String[]> todo() {
    
        return empleadoFacade.mostrarEmpleadoDepartamentoCargo();
    }
    
    @WebMethod(operationName = "todoJPQL")
    public List<String[]> todoJPQL() {
    
        return empleadoFacade.mostrarEmpleadoDepartamentoCargoJPQL();
    }
}
