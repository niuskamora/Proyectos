/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.prueba.control.modelo.entidad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EMPLEADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByEmpleadoid", query = "SELECT e FROM Empleado e WHERE e.empleadoid = :empleadoid"),
    @NamedQuery(name = "Empleado.findByCedula", query = "SELECT e FROM Empleado e WHERE e.cedula = :cedula"),
    @NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleado.findByApellido", query = "SELECT e FROM Empleado e WHERE e.apellido = :apellido"),
    @NamedQuery(name = "Empleado.findBySueldo", query = "SELECT e FROM Empleado e WHERE e.sueldo = :sueldo")})
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCREMENTO_ID_EMPLEADO")
    @SequenceGenerator(name = "INCREMENTO_ID_EMPLEADO", sequenceName = "INCREMENTO_ID_EMPLEADO", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "EMPLEADOID")
    private BigDecimal empleadoid;
    @Basic(optional = false)
    @Column(name = "CEDULA")
    private BigInteger cedula;
    @Size(max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 30)
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "SUELDO")
    private Double sueldo;
    @JoinColumn(name = "DEPARTAMENTOID", referencedColumnName = "DEPARTAMENTOID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento departamentoid;
    @JoinColumn(name = "CARGOID", referencedColumnName = "CARGOID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargo cargoid;
    @OneToMany(mappedBy = "empleadoid", fetch = FetchType.LAZY)
    private List<Proyecto> proyectoList;

    public Empleado() {
    }

    public Empleado(BigDecimal empleadoid) {
        this.empleadoid = empleadoid;
    }

    public Empleado(BigDecimal empleadoid, BigInteger cedula) {
        this.empleadoid = empleadoid;
        this.cedula = cedula;
    }

    public BigDecimal getEmpleadoid() {
        return empleadoid;
    }

    public void setEmpleadoid(BigDecimal empleadoid) {
        this.empleadoid = empleadoid;
    }

    public BigInteger getCedula() {
        return cedula;
    }

    public void setCedula(BigInteger cedula) {
        this.cedula = cedula;
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

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Departamento getDepartamentoid() {
        return departamentoid;
    }

    public void setDepartamentoid(Departamento departamentoid) {
        this.departamentoid = departamentoid;
    }

    public Cargo getCargoid() {
        return cargoid;
    }

    public void setCargoid(Cargo cargoid) {
        this.cargoid = cargoid;
    }

    @XmlTransient
    public List<Proyecto> getProyectoList() {
        return proyectoList;
    }

    public void setProyectoList(List<Proyecto> proyectoList) {
        this.proyectoList = proyectoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadoid != null ? empleadoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.empleadoid == null && other.empleadoid != null) || (this.empleadoid != null && !this.empleadoid.equals(other.empleadoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pangea.prueba.control.modelo.entidad.Empleado[ empleadoid=" + empleadoid + " ]";
    }
    
}
