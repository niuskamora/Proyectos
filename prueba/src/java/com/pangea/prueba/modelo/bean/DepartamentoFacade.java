/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.modelo.bean;

import com.pangea.prueba.control.modelo.entidad.Cargo;
import com.pangea.prueba.control.modelo.entidad.Departamento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PANGEA
 */
@Stateless
public class DepartamentoFacade extends AbstractFacade<Departamento> {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartamentoFacade() {
        super(Departamento.class);
    }
    
    public List<Departamento> retornaDepartamentos(){
        
        List<Departamento> lista=null;
        lista=findAll();
        
        return lista;   
    }
    
    public List<Departamento> listaDepartamentoMayorASeis(){
        
        List<Departamento> listamayoraseis=null;
        listamayoraseis= em.createNamedQuery("Departamento.findByNumeroLetras").getResultList();
        
        return listamayoraseis;
    }
    
    //Promedio Sueldo Departamento SQL
    public List<String[]> promedioSueldoSql(){
        
        List<Object[]> promedioSueldoSql=null;
        promedioSueldoSql=em.createNativeQuery("SELECT departamento.nombre, AVG(empleado.sueldo) FROM empleado INNER JOIN departamento ON empleado.departamentoid = departamento.departamentoid GROUP BY departamento.nombre ").getResultList();
       
        BigDecimal promedio;
        List<String[]> lista=new ArrayList<String[]>();
        
        for (int i = 0; i < promedioSueldoSql.size(); i++) {
            String[] vect=new String[2];
            vect[0]=new String((String) promedioSueldoSql.get(i)[0]);
            promedio= (BigDecimal) promedioSueldoSql.get(i)[1];
            vect[1]=promedio.toString();
            lista.add(vect);
        }
        
        return lista;
    }
   
    //Promedio Sueldo Departamento JPQL
    public List<String[]> promedioSueldo(){
        
        List<Departamento> listaDepartamento=null;
        listaDepartamento=em.createNamedQuery("Departamento.findAll").getResultList();
        
        double suma=0, promedioSueldo=0;
        List<String[]> listaJPQL=new ArrayList<String[]>();
        
        for (int i = 0; i < listaDepartamento.size(); i++) {
            
            String[] vect=new String[2];
            suma=0;
            
            for (int j = 0; j < listaDepartamento.get(i).getEmpleadoList().size(); j++) {
               suma+=listaDepartamento.get(i).getEmpleadoList().get(j).getSueldo();
            }
            
            vect[0]= new String(listaDepartamento.get(i).getNombre());
            promedioSueldo=suma/listaDepartamento.get(i).getEmpleadoList().size();
            vect[1]=String.valueOf(promedioSueldo);
            listaJPQL.add(vect);
        }
        return listaJPQL;
    }
    
    //Cantidad Empleados Departamento SQL
    public List<String[]> cantidadEmpleados(){
    
        List<Object[]> cantidadEmpleados=null;
        cantidadEmpleados=em.createNativeQuery("SELECT departamento.nombre, COUNT(*) FROM departamento, empleado WHERE empleado.departamentoid=departamento.departamentoid GROUP BY departamento.nombre").getResultList();
        
        List<String[]> listaCantidad=new ArrayList<String[]>();
        BigDecimal cantidad;
        
        for (int i = 0; i < cantidadEmpleados.size(); i++) {
            String[] vect=new String[2];
            vect[0]=new String((String) cantidadEmpleados.get(i)[0]);
            cantidad= (BigDecimal) cantidadEmpleados.get(i)[1];
            vect[1]=cantidad.toString();
            listaCantidad.add(vect);
        }
        
        return listaCantidad;
    }
    
    //Cantidad Empleados Departamento JPQL
    public List<String[]> cantidadEmpleadosJPQL(){
    
        List<Departamento> listaDepartamento=null;
        listaDepartamento=em.createNamedQuery("Departamento.findAll").getResultList();
        
        List<String[]> listaCantidadJPQL=new ArrayList<String[]>();
        
        for (int i = 0; i < listaDepartamento.size(); i++) {
            
            String[] vect=new String[2];
            
            vect[0]= new String(listaDepartamento.get(i).getNombre());
            vect[1]=String.valueOf(listaDepartamento.get(i).getEmpleadoList().size());
            listaCantidadJPQL.add(vect);
        }
        return listaCantidadJPQL;
    }
 
    public Departamento descripcionDepartamento(String nombre){
    
       Departamento c=null;
       
       c=(Departamento) em.createNamedQuery("Departamento.findByNombre").setParameter("nombre", nombre).getSingleResult();
       
       return c;
    }
    
}