package repository;

import java.util.List;
import java.util.Optional;

    public interface RepositoryBuku {
        boolean simpan(com.praktikum.testing.model.Buku buku);
        Optional<com.praktikum.testing.model.Buku> cariByIsbn(String isbn);
        List<com.praktikum.testing.model.Buku> cariByJudul(String judul);
        List<com.praktikum.testing.model.Buku> cariByPengarang(String pengarang);
        boolean hapus(String isbn);
        boolean updateJumlahTersedia(String isbn, int jumlahTersediaBaru);
        List<com.praktikum.testing.model.Buku> cariSemua();
    }
}
