package com.praktikum.testing.service;


import com.praktikum.testing.model.Anggota;
import com.praktikum.testing.model.Buku;
import com.praktikum.testing.util.service.RepositoryBuku;
import com.praktikum.testing.util.service.ServicePerpustakaan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test untuk ServicePerpustakaan")
public class ServicePerpustakaanTest {

    @Mock
    private RepositoryBuku mockRepositoryBuku;
    @Mock
    private ApiDenda mockKalkulatorDenda;
    @Mock
    private ApiPengadaan mockApiPengadaan;
    @InjectMocks
    private ServicePerpustakaan servicePerpustakaan;

    private Buku bukuTest;
    private Anggota anggotaTest;

    @BeforeEach
    void setUp() {
        // servicePerpustakaan = new ServicePerpustakaan(mockRepositoryBuku, mockKalkulatorDenda); // Inisialisasi bisa dihandle oleh @InjectMocks
        bukuTest = new Buku("1234567890", "Pemrograman Java", "John Doe", 3, 150000.0);
        anggotaTest = new Anggota("A001", "John Student", "john@student.ac.id", "081234567890", Anggota.TipeAnggota.MAHASISWA);
    }

    // --- TEST TAMBAH BUKU ---

    @Test
    @DisplayName("Tambah buku berhasil ketika data valid dan buku belum ada")
    void testTambahBukuBerhasil() {
        // Arrange - Mock behavior
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.empty());
        when(mockRepositoryBuku.simpan(bukuTest)).thenReturn(true);

        // Act
        boolean hasil = servicePerpustakaan.tambahBuku(bukuTest);

        // Assert
        assertTrue(hasil, "Harus berhasil menambah buku");
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku).simpan(bukuTest);
    }

    @Test
    @DisplayName("Tambah buku gagal ketika buku sudah ada")
    void testTambahBukuGagalBukuSudahAda() {
        // Arrange
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act
        boolean hasil = servicePerpustakaan.tambahBuku(bukuTest);

        // Assert
        assertFalse(hasil, "Tidak boleh menambah buku yang sudah ada");
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku, never()).simpan(any(Buku.class));
    }

    @Test
    @DisplayName("Tambah buku gagal ketika data tidak valid")
    void testTambahBukuGagalDataTidakValid() {
        // Arrange
        Buku bukuTidakValid = new Buku("123", "", " ", 0, -100.0);

        // Act
        boolean hasil = servicePerpustakaan.tambahBuku(bukuTidakValid);

        // Assert
        assertFalse(hasil, "Tidak boleh menambah buku dengan data tidak valid");
        verifyNoInteractions(mockRepositoryBuku);
    }

    // --- TEST HAPUS BUKU ---

    @Test
    @DisplayName("Hapus buku berhasil ketika tidak ada yang dipinjam")
    void testHapusBukuBerhasil() {
        // Arrange
        bukuTest.setJumlahTersedia(5); // Semua salinan tersedia
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));
        when(mockRepositoryBuku.hapus("1234567890")).thenReturn(true);

        // Act
        boolean hasil = servicePerpustakaan.hapusBuku("1234567890");

        // Assert
        assertTrue(hasil, "Harus berhasil menghapus buku");
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku).hapus("1234567890");
    }

    @Test
    @DisplayName("Hapus buku gagal ketika ada yang dipinjam")
    void testHapusBukuGagalAdaYangDipinjam() {
        // Arrange
        bukuTest.setJumlahTersedia(2); // Ada yang dipinjam (5 total - 2 tersedia = 3 dipinjam)
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act
        boolean hasil = servicePerpustakaan.hapusBuku("1234567890");

        // Assert
        assertFalse(hasil, "Tidak boleh menghapus buku yang sedang dipinjam");
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku, never()).hapus(anyString());
    }

    // --- TEST CARI BUKU ---

    @Test
    @DisplayName("Cari buku by ISBN berhasil")
    void testCariBukuByIsbnBerhasil() {
        // Arrange
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act
        Optional<Buku> hasil = servicePerpustakaan.cariBukuByIsbn("1234567890");

        // Assert
        assertTrue(hasil.isPresent(), "Harus menemukan buku");
        assertEquals("Pemrograman Java", hasil.get().getJudul());
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
    }

    @Test
    @DisplayName("Cari buku by judul berhasil")
    void testCariBukuByJudul() {
        // Arrange
        List<Buku> daftarBuku = Arrays.asList(bukuTest);
        when(mockRepositoryBuku.cariByJudul("Java")).thenReturn(daftarBuku);

        // Act
        List<Buku> hasil = servicePerpustakaan.cariBukuByJudul("Java");

        // Assert
        assertEquals(1, hasil.size());
        assertEquals("Pemrograman Java", hasil.get(0).getJudul());
        verify(mockRepositoryBuku).cariByJudul("Java");
    }

    // --- TEST PINJAM BUKU ---

    @Test
    @DisplayName("Pinjam buku berhasil ketika semua kondisi terpenuhi")
    void testPinjamBukuBerhasil() {
        // Arrange
        bukuTest.setJumlahTersedia(1);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));
        when(mockRepositoryBuku.updateJumlahTersedia("1234567890", 0)).thenReturn(true);

        // Act
        boolean hasil = servicePerpustakaan.pinjamBuku("1234567890", anggotaTest);

        // Assert
        assertTrue(hasil, "Harus berhasil meminjam buku");
        assertTrue(anggotaTest.getIsbnBukuDipinjam().contains("1234567890"));
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku).updateJumlahTersedia("1234567890", 0);
    }

    @Test
    @DisplayName("Pinjam buku gagal ketika buku tidak tersedia")
    void testPinjamBukuGagalTidakTersedia() {
        // Arrange
        bukuTest.setJumlahTersedia(0);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act
        boolean hasil = servicePerpustakaan.pinjamBuku("1234567890", anggotaTest);

        // Assert
        assertFalse(hasil, "Tidak boleh meminjam buku yang tidak tersedia");
        assertFalse(anggotaTest.getIsbnBukuDipinjam().contains("1234567890"));
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku, never()).updateJumlahTersedia(anyString(), anyInt());
    }

    @Test
    @DisplayName("Pinjam buku gagal ketika anggota tidak aktif")
    void testPinjamBukuGagalAnggotaTidakAktif() {
        // Arrange
        anggotaTest.setAktif(false);

        // Act
        boolean hasil = servicePerpustakaan.pinjamBuku("1234567890", anggotaTest);

        // Assert
        assertFalse(hasil, "Anggota tidak aktif tidak boleh meminjam buku");
        verifyNoInteractions(mockRepositoryBuku);
    }

    @Test
    @DisplayName("Pinjam buku gagal ketika batas pinjam tercapai")
    void testPinjamBukuGagalBatasPinjamTercapai() {
        // Arrange
        // Anggota sudah pinjam 3 buku (batas maksimal)
        anggotaTest.tambahBukuDipinjam("3333333333");
        anggotaTest.tambahBukuDipinjam("2222222222");
        anggotaTest.tambahBukuDipinjam("1111111111");

        // Act
        boolean hasil = servicePerpustakaan.pinjamBuku("1234567890", anggotaTest);

        // Assert
        assertFalse(hasil, "Tidak boleh meminjam ketika batas pinjam tercapai");
        verifyNoInteractions(mockRepositoryBuku);
    }

    // --- TEST KEMBALIKAN BUKU ---

    @Test
    @DisplayName("Kembalikan buku berhasil")
    void testKembalikanBukuBerhasil() {
        // Arrange
        anggotaTest.tambahBukuDipinjam("1234567890");
        bukuTest.setJumlahTersedia(2);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));
        when(mockRepositoryBuku.updateJumlahTersedia("1234567890", 3)).thenReturn(true);

        // Act
        boolean hasil = servicePerpustakaan.kembalikanBuku("1234567890", anggotaTest);

        // Assert
        assertTrue(hasil, "Harus berhasil mengembalikan buku");
        assertFalse(anggotaTest.getIsbnBukuDipinjam().contains("1234567890"));
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
        verify(mockRepositoryBuku).updateJumlahTersedia("1234567890", 3);
    }

    @Test
    @DisplayName("Kembalikan buku gagal ketika anggota tidak meminjam buku tersebut")
    void testKembalikanBukuGagalTidakMeminjam() {
        // Arrange
        // Anggota tidak meminjam "1234567890"

        // Act
        boolean hasil = servicePerpustakaan.kembalikanBuku("1234567890", anggotaTest);

        // Assert
        assertFalse(hasil, "Tidak boleh mengembalikan buku yang tidak dipinjam");
        verifyNoInteractions(mockRepositoryBuku);
    }

    // --- TEST BUKU TERSEDIA ---

    @Test
    @DisplayName("Buku tersedia")
    void testBukuTersedia() {
        // Arrange - Buku Tersedia
        bukuTest.setJumlahTersedia(1);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act & Assert
        assertTrue(servicePerpustakaan.bukuTersedia("1234567890"));

        // Arrange - Buku Tidak tersedia
        bukuTest.setJumlahTersedia(0);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act & Assert
        assertFalse(servicePerpustakaan.bukuTersedia("1234567890"));
    }

    // --- TEST GET JUMLAH TERSEDIA ---

    @Test
    @DisplayName("Get jumlah tersedia")
    void testGetJumlahTersedia() {
        // Arrange
        bukuTest.setJumlahTersedia(5);
        when(mockRepositoryBuku.cariByIsbn("1234567890")).thenReturn(Optional.of(bukuTest));

        // Act
        int jumlah = servicePerpustakaan.getJumlahTersedia("1234567890");

        // Assert
        assertEquals(5, jumlah);
        verify(mockRepositoryBuku).cariByIsbn("1234567890");
    }

    @Test
    @DisplayName("Get jumlah tersedia untuk buku yang tidak ada")
    void testGetJumlahTersediaBukuTidakAda() {
        // Arrange
        when(mockRepositoryBuku.cariByIsbn("9999999999")).thenReturn(Optional.empty());

        // Act
        int jumlah = servicePerpustakaan.getJumlahTersedia("9999999999");

        // Assert
        assertEquals(0, jumlah);
        verify(mockRepositoryBuku).cariByIsbn("9999999999");
    }

}
