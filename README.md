# Kelime Tahmin (Adam Asmaca) Oyunu - Java Swing

Bu proje, Java'nın **Swing (JFrame)** kütüphanesi kullanılarak geliştirilmiş, dinamik dosya yönetimi ve loglama mekanizmasına sahip bir **Kelime Tahmin (Adam Asmaca)** oyunudur. Oyuncular harf veya kelime tahmini yaparak sistemin havuzdan rastgele seçtiği gizli kelimeyi bulmaya çalışır.

---

##  Özellikler

* **Dinamik Kelime Havuzu:** Oyun her başladığında, belirtilen bir `.txt` dosyasından rastgele kelimeler seçer.
* **Görsel Geri Bildirim:** Kullanıcının yaptığı her yanlış tahminde (maksimum 11 hak), can durumunu gösteren görseller (`.jpg`) dinamik olarak güncellenir.
* **Zamanlayıcı (Timer):** Oyun esnasında geçen süre saniye cinsinden anlık olarak ekranda gösterilir.
* **Skor ve Geçmiş Yönetimi:**
    * **Eski Skorlar:** Tamamlanan her oyunun tarihi, aranan kelimesi, kazanma/kaybetme durumu ve harcanan süre `oyunlar.txt` dosyasına kaydedilir ve tablo üzerinden listelenir.
    * **Giriş Logları:** Oyuna dair kritik zaman damgaları `log.txt` dosyasında tutulur ve arayüzden takip edilebilir.
* **Güvenli Yönetici Paneli:** Skor tablosunu ve log geçmişini temizlemek isteyen kullanıcıların, admin şifresi (`1234`) doğrulaması yapması gerekir.

---

## 📂 Klasör Yapısı ve Dosya Gereksinimleri

Projenin sorunsuz çalışabilmesi için kaynak dosyaların proje dizininde veya erişilebilir bir konumda bulunması gerekir. Dosyaların aşağıdaki hiyerarşide düzenlenmesi önerilir:

```text
P2OYUN/
│
├── txtdosyalar/
│   ├── kelimeler.txt   # Oyunda sorulacak kelimelerin listesi (Her satıra bir kelime)
│   ├── oyunlar.txt     # Eski skorların otomatik kaydedildiği dosya
│   └── log.txt         # Giriş loglarının tutulduğu dosya
│
└── resimler/
    ├── 1.jpg           # 1. Yanlış tahminde görünecek görsel
    ├── 2.jpg           # 2. Yanlış tahminde görünecek görsel
    └── ...             # 11.jpg'ye kadar devam eden görsel seti
