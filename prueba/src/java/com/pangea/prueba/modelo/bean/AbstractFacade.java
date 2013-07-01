/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.modelo.bean;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author toshiba
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    
   public List<T> findRange(String _sortField, boolean _sortOrder, int[] _range, Map<String, String> _filters) {

      return findRange(_sortField, _sortOrder, _range, _filters, "");
   }

   public List<T> findRange(String _sortField, boolean _sortOrder, int[] _range, Map<String, String> _filters, String cadena) {
      javax.persistence.Query q;
//        String condi = getCondicion(entityClass.getSimpleName());
//        System.out.println(condi);

      System.out.println("cadena en facade: " + cadena);

//        if (entityClass.getSimpleName().compareTo("Mvcli001m") == 0) {
//            StringBuffer sentencia = new StringBuffer("select u").append(" from ").append(entityClass.getSimpleName()).append(" u ").append(" where u.mvofc001m.mvofc001mPK.codof in ('4286','4284','4288')");
      StringBuffer sentencia = new StringBuffer("select u").append(" from ").append(entityClass.getSimpleName()).append(" u ");
      /**
       * Para desconcatenar un mapa
       */
      if (_filters != null) {
         Iterator it = _filters.entrySet().iterator();
         if (_filters.size() > 0) {
            sentencia.append(" where ");
         }

         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();

            // si el filtro es de tipo date no se le aplica UPPER
            if (e.getValue() instanceof java.util.Date) {
               System.out.println("-------------se recibe par date: " + e.getValue().toString());
               String dateValue = new SimpleDateFormat("yyyy/MM/dd").format(e.getValue());
               sentencia.append(" u.").append(e.getKey()).append(" IS NOT NULL AND");
               sentencia.append(" u.").append(e.getKey()).append(" = \"").append(dateValue).append("\" and ");
            } else {
               sentencia.append(" UPPER(u.").append(e.getKey()).append(") like '").append(e.getValue().toString().toUpperCase()).append("%' and ");
            }
            System.out.println(e.getKey() + " Paramo " + e.getValue());
         }
         int lastIndexOfAnd = sentencia.lastIndexOf("and");
         if (lastIndexOfAnd != -1) {
            sentencia = new StringBuffer(sentencia.substring(0, lastIndexOfAnd));
         } else {
            //System.out.println("no tiene and");
         }
      }

      // esto se hace posterior a la carga de filtros
      //si sentencia tiene where --> cambiar el where por and
      //si sentencia no tiene where  -->dejar el where
      //-----------------
      if (cadena != null && !cadena.equals("")) {


         if (sentencia.indexOf("where") != -1) {       //si contiene where
            sentencia.append(" and ");
            sentencia.append(cadena);
            sentencia.append(" ");
         } else {
            sentencia.append(" where ");
            sentencia.append(cadena);
            sentencia.append(" ");
         }
      }
      if (_sortField != null && _sortField != null) {
         sentencia.append(" order by u.").append(_sortField);
         if (_sortOrder) {
            sentencia.append(" asc");
         } else {
            sentencia.append(" desc");
         }
      }
      System.out.println(sentencia);

      q = getEntityManager().createQuery(sentencia.toString());
      q.setMaxResults(_range[1] - _range[0]);
      q.setFirstResult(_range[0]);


      return q.getResultList();
   }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}