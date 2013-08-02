/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.modelo.bean;

import com.pangea.prueba.control.modelo.entidad.Empleado;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PANGEA
 */
@Stateless
public class EmpleadoFacade extends AbstractFacade<Empleado> {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpleadoFacade() {
        super(Empleado.class);
    }
    //Promedio Sueldo General
    public BigDecimal promedioSueldoGeneral(){
        
        BigDecimal promedioSueldoG;
        promedioSueldoG= (BigDecimal) em.createNativeQuery("SELECT  AVG(sueldo) FROM empleado").getSingleResult();
       
        return promedioSueldoG;
    }
    
   
    
    //Empleado con Departamento y Cargo
    
    public List<String[]> mostrarEmpleadoDepartamentoCargo(){
    
        List<Object[]> mostrarTodo=null;
        mostrarTodo=em.createNativeQuery("SELECT empleado.nombre, departamento.nombre, cargo.nombre  FROM departamento, empleado, cargo WHERE empleado.departamentoid=departamento.departamentoid and empleado.cargoid=cargo.cargoid").getResultList();
        
        List<String[]> lista=new ArrayList<String[]>();
        
        for (int i = 0; i < mostrarTodo.size(); i++) {
            String[] vect=new String[3];
            vect[0]=new String ((String) mostrarTodo.get(i)[0]);
            vect[1]=new String ((String) mostrarTodo.get(i)[1]);
            vect[2]=new String ((String) mostrarTodo.get(i)[2]);
            
            lista.add(vect);
        }
        
        return lista;
    }
    
    //Empleado con Departamento y Cargo con JPQL
    
    public List<String[]> mostrarEmpleadoDepartamentoCargoJPQL(){
    
        List<Empleado> mostrarTodoJPQL=null;
        mostrarTodoJPQL=em.createNamedQuery("Empleado.findAll").getResultList();
        List<String[]> lista=new ArrayList<String[]>();
        
        for (int i = 0; i < mostrarTodoJPQL.size(); i++) {
            
            String[] vect=new String[3];
            vect[0]= mostrarTodoJPQL.get(i).getNombre();
            vect[1]= mostrarTodoJPQL.get(i).getDepartamentoid().getNombre();
            vect[2]= mostrarTodoJPQL.get(i).getCargoid().getNombre();
            
            lista.add(vect);
        }
        
        return lista;
    }
    
    
    public Empleado todoEmpleado(int cedula){
    
       Empleado emp=null;
       
       emp=(Empleado) em.createNamedQuery("Empleado.findByEmpleadoid").setParameter("empleadoid", cedula).getSingleResult();
       
       return emp;
    }
    
     public void insertarEmpleado(Empleado registroe){
    
        this.create(registroe);
    }
}
