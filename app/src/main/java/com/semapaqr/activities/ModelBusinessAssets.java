package com.semapaqr.activities;

public class ModelBusinessAssets {

    String id, codigo_activo, nombre_activo, tipo_activo, descripcion_activo, sector_activo,
        encargado_activo, addedTime, updatedTime;

    public ModelBusinessAssets(String id, String codigo_activo, String nombre_activo, String tipo_activo,
                               String descripcion_activo, String sector_activo,
                               String encargado_activo, String addedTime, String updatedTime) {
        this.id = id;
        this.codigo_activo = codigo_activo;
        this.nombre_activo = nombre_activo;
        this.tipo_activo = tipo_activo;
        this.descripcion_activo = descripcion_activo;
        this.sector_activo = sector_activo;
        this.encargado_activo = encargado_activo;
        this.addedTime = addedTime;
        this.updatedTime = updatedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo_activo() {
        return codigo_activo;
    }

    public void setCodigo_activo(String codigo_activo) {
        this.codigo_activo = codigo_activo;
    }

    public String getNombre_activo() {
        return nombre_activo;
    }

    public void setNombre_activo(String nombre_activo) {
        this.nombre_activo = nombre_activo;
    }

    public String getTipo_activo() {
        return tipo_activo;
    }

    public void setTipo_activo(String tipo_activo) {
        this.tipo_activo = tipo_activo;
    }

    public String getDescripcion_activo() {
        return descripcion_activo;
    }

    public void setDescripcion_activo(String descripcion_activo) {
        this.descripcion_activo = descripcion_activo;
    }

    public String getSector_activo() {
        return sector_activo;
    }

    public void setSector_activo(String sector_activo) {
        this.sector_activo = sector_activo;
    }

    public String getEncargado_activo() {
        return encargado_activo;
    }

    public void setEncargado_activo(String encargado_activo) {
        this.encargado_activo = encargado_activo;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
