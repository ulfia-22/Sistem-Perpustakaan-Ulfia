package repository;

import com.praktikum.testing.model.Buku;

import java.util.List;
import java.util.Optional;

interface RepositoryBuku {
    boolean simpan(com.praktikum.testing.model.Buku buku);
    Optional<Buku> cariByIsbn(String isbn);
    List<Buku> cariByJudul(String judul);
    List<com.praktikum.testing.model.Buku> cariByPengarang(String pengarang);
    boolean hapus(String isbn);

    List<com.praktikum.testing.model.Buku> cariSemua();
}




