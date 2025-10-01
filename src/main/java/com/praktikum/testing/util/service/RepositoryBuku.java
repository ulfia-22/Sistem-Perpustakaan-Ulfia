package com.praktikum.testing.util.service;

import com.praktikum.testing.model.Buku;

import java.util.List;
import java.util.Optional;

public interface RepositoryBuku {
    boolean simpan(Buku buku);

    Optional<Buku> cariByIsbn(String isbn);

    boolean hapus(String isbn);

    List<Buku> cariByJudul(String judul);

    List<Buku> cariByPengarang(String pengarang);

    boolean updateJumlahTersedia(String isbn, int i);

    List<Buku> cariSemua();
}
