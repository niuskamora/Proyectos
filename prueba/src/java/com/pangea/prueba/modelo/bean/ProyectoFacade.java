/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.modelo.bean;

import com.pangea.prueba.control.modelo.entidad.Proyecto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PANGEA
 */
@Stateless
public class ProyectoFacade extends AbstractFacade<Proyecto> {
    @PersistenceContext(unitName = "pruebaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoFacade() {
        super(Proyecto.class);
    }
    
    public Proyecto todoProyecto(String nombre){
    
       Proyecto proy=null;
       
       proy=(Proyecto) em.createNamedQuery("Proyecto.findByNombre").setParameter("nombre", nombre).getSingleResult();
       
       return proy;
    }
    
     public void insertarProyecto(Proyecto registrop){
    
        this.create(registrop);
    }
}
