package com.praktikum.testing.service;

import com.praktikum.testing.model.Anggota;
import com.praktikum.testing.model.Peminjaman;
import com.praktikum.testing.util.service.KalkulatorDenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Kalkulator Denda")
public class CalculatorAttended {
    private KalkulatorDenda kalkulatorDenda;
    private Anggota anggotaMahasiswa;
    private Anggota anggotaDosen;
    private Anggota anggotaUmum;

    @BeforeEach
    void setUp() {
        kalkulatorDenda = new KalkulatorDenda();
        anggotaMahasiswa = new Anggota("M001", "John Student", "john@student.ac.id",
                "081234567890", Anggota.TipeAnggota.MAHASISWA);

        anggotaDosen = new Anggota("D001", "Dr. Faculty", "faculty@univ.ac.id",
                "081234567891", Anggota.TipeAnggota.DOSEN);

        anggotaUmum = new Anggota("U001", "Public User", "public@gmail.com",
                "081234567892", Anggota.TipeAnggota.UMUM);
    }

    @Test
    @DisplayName("Tidak ada denda untuk peminjaman yang tidak terlambat")
    void testHitungDendaUntukPeminjamanTidakTerlambat() {
        LocalDate tanggalPinjam = LocalDate.now().minusDays(5);
        LocalDate tanggalJatuhTempo = LocalDate.now().plusDays(2);

        Peminjaman peminjaman = new Peminjaman("F001", "M001", "1234567890",
                tanggalPinjam, tanggalJatuhTempo);

        double denda = kalkulatorDenda.hitungDenda(peminjaman, anggotaMahasiswa);
        assertEquals(0.0, denda, "Denda harus 0 untuk peminjaman yang tidak terlambat");
    }

    @Test
    @DisplayName("Hitung denda mahasiswa 3 hari terlambat")
    void testHitungDendaMahasiswaTigaHariTerlambat() {
        LocalDate tanggalPinjam = LocalDate.now().minusDays(10);
        LocalDate tanggalJatuhTempo = LocalDate.now().minusDays(3); // 3 hari terlambat

        Peminjaman peminjaman = new Peminjaman("F001", "M001", "1234567890",
                tanggalPinjam, tanggalJatuhTempo);

        double dendaAktual = kalkulatorDenda.hitungDenda(peminjaman, anggotaMahasiswa);
        assertEquals(3000.0, dendaAktual, "3 hari × 1000 harus sama dengan 3000");
    }

    @Test
    @DisplayName("Hitung denda dosen 5 hari terlambat")
    void testHitungDendaDosen() {
        LocalDate tanggalPinjam = LocalDate.now().minusDays(12);
        LocalDate tanggalJatuhTempo = LocalDate.now().minusDays(5); // 5 hari terlambat

        Peminjaman peminjaman = new Peminjaman("F001", "D001", "1234567890",
                tanggalPinjam, tanggalJatuhTempo);

        double dendaAktual = kalkulatorDenda.hitungDenda(peminjaman, anggotaDosen);
        assertEquals(10000.0, dendaAktual, "5 hari × 2000 harus sama dengan 10000");
    }

    @Test
    @DisplayName("Denda tidak boleh melebihi batas maksimal")
    void testDendaTidakMelebihiBatasMaximal() {
        // Peminjaman sangat terlambat (100 hari)
        LocalDate tanggalPinjam = LocalDate.now().minusDays(107);
        LocalDate tanggalJatuhTempo = LocalDate.now().minusDays(100);

        Peminjaman peminjaman = new Peminjaman("F001", "M001", "1234567890",
                tanggalPinjam, tanggalJatuhTempo);

        double dendaAktual = kalkulatorDenda.hitungDenda(peminjaman, anggotaMahasiswa);
        assertEquals(50000.0, dendaAktual, "Denda tidak boleh melebihi batas maksimal mahasiswa");
    }

    @Test
    @DisplayName("Exception untuk parameter null")
    void testExceptionParameterNull() {
        assertThrows(IllegalArgumentException.class,
                () -> kalkulatorDenda.hitungDenda(null, anggotaMahasiswa),
                "Harus throw exception untuk peminjaman null");

        Peminjaman peminjaman = new Peminjaman("F001", "M001", "1234567890",
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));

        assertThrows(IllegalArgumentException.class,
                () -> kalkulatorDenda.hitungDenda(peminjaman, null),
                "Harus throw exception untuk anggota null");
    }

    @Test
    @DisplayName("Get denda maksimal sesuai tipe anggota")
     void testGetDendaMaximal() {
        assertEquals(50000.0, kalkulatorDenda.getDendaMaximal(Anggota.TipeAnggota.MAHASISWA));
        assertEquals(100000.0, kalkulatorDenda.getDendaMaximal(Anggota.TipeAnggota.DOSEN));
        assertEquals(50000.0, kalkulatorDenda.getDendaMaximal(Anggota.TipeAnggota.UMUM));

        assertThrows(IllegalArgumentException.class,
                () -> kalkulatorDenda.getDendaMaximal(null));
    }

    @Test
    @DisplayName("Cek ada denda")
    void testAdaDenda() {
        // peminjaman terlambat
        LocalDate tanggalJatuhTempo = LocalDate.now().minusDays(1);

        Peminjaman peminjamanTerlambat = new Peminjaman("F001", "M001", "1234567890",
                LocalDate.now().minusDays(6), tanggalJatuhTempo);

        double denda = kalkulatorDenda.hitungDenda(peminjamanTerlambat, anggotaMahasiswa);
        assertTrue(denda > 0, "Seharusnya ada denda untuk keterlambatan");
    }

    public Anggota getAnggotaUmum() {
        return anggotaUmum;
    }

    public void setAnggotaUmum(Anggota anggotaUmum) {
        this.anggotaUmum = anggotaUmum;
    }
}

