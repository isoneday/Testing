
package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelKategoriMakanan {

    @SerializedName("DataKategori")
    @Expose
    private List<DataKategorus> dataKategori = null;

    public List<DataKategorus> getDataKategori() {
        return dataKategori;
    }

    public void setDataKategori(List<DataKategorus> dataKategori) {
        this.dataKategori = dataKategori;
    }

}
