/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.modelo.bean;

import com.pangea.prueba.control.modelo.entidad.Cargo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PANGEA
 */
@Stateless
public class CargoFacade extends AbstractFacade<Cargo> {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CargoFacade() {
        super(Cargo.class);
    }
    
    public List<Cargo> retornaCargos(){
        
        List<Cargo> lista=null;
        
        lista=findAll();
        
        return lista;
        
        
    }
    public Cargo descripcionCargo(String nombre){
    
       Cargo c=null;
       
       c=(Cargo) em.createNamedQuery("Cargo.findByNombre").setParameter("nombre", nombre).getSingleResult();
       
       return c;
    }
    
    public void insertarCargo(Cargo registroc){
    
        this.create(registroc);
    }
    
}