/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.control.modelo.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PANGEA
 */
@Entity
@Table(name = "CARGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cargo.findAll", query = "SELECT c FROM Cargo c"),
    @NamedQuery(name = "Cargo.findByCargoid", query = "SELECT c FROM Cargo c WHERE c.cargoid = :cargoid"),
    @NamedQuery(name = "Cargo.findByNombre", query = "SELECT c FROM Cargo c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cargo.findByDescripcion", query = "SELECT c FROM Cargo c WHERE c.descripcion = :descripcion")})
public class Cargo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARGOSEQ")
    @SequenceGenerator(name = "CARGOSEQ", sequenceName = "CARGO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CARGOID")
    private BigDecimal cargoid;
    @Size(max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 120)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "cargoid", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Empleado> empleadoList;

    public Cargo() {
    }

    public Cargo(BigDecimal cargoid) {
        this.cargoid = cargoid;
    }

    public BigDecimal getCargoid() {
        return cargoid;
    }

    public void setCargoid(BigDecimal cargoid) {
        this.cargoid = cargoid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cargoid != null ? cargoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargo)) {
            return false;
        }
        Cargo other = (Cargo) object;
        if ((this.cargoid == null && other.cargoid != null) || (this.cargoid != null && !this.cargoid.equals(other.cargoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pangea.prueba.control.modelo.entidad.Cargo[ cargoid=" + cargoid + " ]";
    }
    
}
