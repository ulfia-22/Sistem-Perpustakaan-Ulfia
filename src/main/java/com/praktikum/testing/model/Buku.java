package com.praktikum.testing.model;

import java.util.Objects;

public class Buku {
        private String isbn;
        private String judul;
        private String pengarang;
        private int jumlahTotal;
        private int jumlahTersedia;
        private double harga;

        public Buku(String number, String pemrogramanJava, String johnDoe, int i, double v) {
        }

        public Buku(String isbn, String judul, String pengarang, int jumlahTotal, int jumlahTersedia, double harga) {
            this.isbn = isbn;
            this.judul = judul;
            this.pengarang = pengarang;
            this.jumlahTotal = jumlahTotal;
            this.jumlahTersedia = jumlahTersedia;
            this.harga = harga;
        }

    public Buku() {
      // Inisialisasi default jika diperlukan
    }

    // Getters dan Setters
        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getJudul() {
            return judul;
        }

        public void setJudul(String judul) {
            this.judul = judul;
        }

        public String getPengarang() {
            return pengarang;
        }

        public void setPengarang(String pengarang) {
            this.pengarang = pengarang;
        }

        public int getJumlahTotal() {
            return jumlahTotal;
        }

        public void setJumlahTotal(int jumlahTotal) {
            this.jumlahTotal = jumlahTotal;
        }

        public int getJumlahTersedia() {
            return jumlahTersedia;
        }

        public void setJumlahTersedia(int jumlahTersedia) {
            this.jumlahTersedia = jumlahTersedia;
        }

        public double getHarga() {
            return harga;
        }

        public void setHarga(double harga) {
            this.harga = harga;
        }

        public boolean isTersedia() {
            return jumlahTersedia > 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Buku buku = (Buku) o;
            return Objects.equals(isbn, buku.isbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(isbn);
        }

        @Override
        public String toString() {
            return "Buku{" +
                    "isbn='" + isbn + '\'' +
                    ", judul='" + judul + '\'' +
                    ", pengarang='" + pengarang + '\'' +
                    ", jumlahTotal=" + jumlahTotal +
                    ", jumlahTersedia=" + jumlahTersedia +
                    ", harga=" + harga +
                    '}';
        }

    }
