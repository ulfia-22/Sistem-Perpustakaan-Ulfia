package com.praktikum.testing.util;


import com.praktikum.testing.model.Anggota;
import com.praktikum.testing.model.Buku;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test Validasi Utils")
public class ValidationUtilsTest {
    @Test
    @DisplayName("Email valid harus mengembalikan true")
    void testEmailValid() {
        assertTrue(ValidationUtils.isValidEmail("mahasiswa@univ.ac.id"));
        assertTrue(ValidationUtils.isValidEmail("test@gmail.com"));
        assertTrue(ValidationUtils.isValidEmail("user123@domain.org"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "email-tanpa-at.com", "email@", "@domain.com", "email@domain.com."})
    @DisplayName("Email tidak valid harus mengembalikan false")
    void testEmailTidakValid(String emailTidakValid) {
        assertFalse(ValidationUtils.isValidEmail(emailTidakValid));
    }

    @Test
    @DisplayName("Email null harus mengembalikan false")
    void testEmailNull() {
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    @DisplayName("Nomor telepon valid harus mengembalikan true")
    void testNomorTeleponValid() {
        assertTrue(ValidationUtils.isValidNomorTelepon("081234567890"));
        assertTrue(ValidationUtils.isValidNomorTelepon("+628123456789"));
        assertTrue(ValidationUtils.isValidNomorTelepon("0812-3456-7890"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "123456789", "07123456789", "081234", "+627123456789"})
    @DisplayName("Nomor telepon tidak valid harus mengembalikan false")
    void testNomorTeleponTidakValid(String teleponTidakValid) {
        assertFalse(ValidationUtils.isValidNomorTelepon(teleponTidakValid));
    }

    @Test
    @DisplayName("ISBN valid harus mengembalikan true")
    void testISBNValid() {
        assertTrue(ValidationUtils.isValidISBN("1234567890"));
        assertTrue(ValidationUtils.isValidISBN("1234567890123"));
        assertTrue(ValidationUtils.isValidISBN("123-456-789-0"));
    }

    @Test
    @DisplayName("Buku valid harus mengembalikan true")
    void testBukuValid() {
        Buku buku = new Buku("1234567890", "Pemrograman Java", "John Doe", 5, 150000.0);
        assertTrue(ValidationUtils.isValidBuku(buku));
    }

    @Test
    @DisplayName("Buku dengan data tidak valid harus mengembalikan false")
    void testBukuTidakValid() {
        // Buku null
        assertFalse(ValidationUtils.isValidBuku(null));

        // ISBN tidak valid
        Buku bukuIsbnTidakValid = new Buku("123", "Judul", "Pengarang", 5, 100000.0);
        assertFalse(ValidationUtils.isValidBuku(bukuIsbnTidakValid));

        // Jumlah total negatif
        Buku bukuJumlahNegatif = new Buku("1234567890", "Judul", "Pengarang", -1, 100000.0);
        assertFalse(ValidationUtils.isValidBuku(bukuJumlahNegatif));

        // Harga negatif
        Buku bukuHargaNegatif = new Buku("1234567890", "Judul", "Pengarang", 5, -100000.0);
        assertFalse(ValidationUtils.isValidBuku(bukuHargaNegatif));
    }

    @Test
    @DisplayName("Anggota valid harus mengembalikan true")
    void testAnggotaValid() {
        Anggota anggota = new Anggota("A001", "John Doe", "John@univ.ac.id", "081234567890", Anggota.TipeAnggota.MAHASISWA);
        assertTrue(ValidationUtils.isValidAnggota(anggota));
    }

    @Test
    @DisplayName("String valid harus mengembalikan true")
    void testStringValid() {
        assertTrue(ValidationUtils.isValidString("teks"));
        assertTrue(ValidationUtils.isValidString("teks dengan spasi"));

        assertFalse(ValidationUtils.isValidString(""));
        assertFalse(ValidationUtils.isValidString(" "));
        assertFalse(ValidationUtils.isValidString(null));
    }

    @Test
    @DisplayName("Angka positif dan non-negatif harus valid")
    void testValidasiAngka() {
        // Angka Positif
        assertTrue(ValidationUtils.isAngkaPositif(1.0));
        assertTrue(ValidationUtils.isAngkaPositif(100.5));
        assertTrue(ValidationUtils.isAngkaPositif(0.01));
        assertFalse(ValidationUtils.isAngkaPositif(-1.0));

        // Angka Non-Negatif
        assertTrue(ValidationUtils.isAngkaNonNegatif(1.0));
        assertTrue(ValidationUtils.isAngkaNonNegatif(0.0));
        assertFalse(ValidationUtils.isAngkaNonNegatif(-0.1));
    }
}

