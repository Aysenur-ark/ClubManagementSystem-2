# 🏛️ Kulüp Yönetim Sistemi (Club Management System)

Bu proje, bir üniversite veya topluluk bünyesindeki kulüp üyelerini, etkinlikleri ve katılım durumlarını yönetmek amacıyla geliştirilmiş **3 Katmanlı Mimariye (3-Tier Architecture)** sahip bir masaüstü uygulamasıdır.

Proje, yazılım mimarisi standartlarına uygun olarak kodun sürdürülebilirliğini ve test edilebilirliğini artıracak şekilde iş mantığı (Business Logic), veri erişim (DAO) ve arayüz (GUI) katmanlarına ayrılarak tasarlanmıştır.

---

## 🛠️ Kullanılan Teknolojiler

* **Programlama Dili:** Java (JDK 17+)
* **Kullanıcı Arayüzü:** Java Swing
* **Veritabanı:** MySQL / SQLite
* **Veri Erişimi:** JDBC & DAO (Data Access Object) deseni
* **Bağımlılık Yönetimi:** Maven

---

## ✨ Öne Çıkan Özellikler

* **Üye Yönetimi:** Kulüp üyelerinin eklenmesi, güncellenmesi, silinmesi ve listelenmesi.
* **Etkinlik & Katılım Takibi:** Düzenlenen etkinliklerin kaydı ve üye katılım durumlarının kontrolü.
* **Katmanlı Mimari:** Arayüz, iş mantığı ve veritabanı sorgularının birbirinden bağımsız yönetilmesi.
* **Kullanıcı Dostu Arayüz:** Swing bileşenleri ile tasarlanmış pratik masaüstü yönetim paneli.

---

## 🏗️ Proje Mimarisi (3-Tier Architecture)

Proje yapısı aşağıdaki katmanlardan oluşmaktadır:

1. **Presentation Layer (View):** Kullanıcı etkileşimini sağlayan Java Swing ekranları.
2. **Business Logic Layer (Service):** İş kurallarının ve doğrulama süreçlerinin yürütüldüğü katman.
3. **Data Access Layer (DAO / Model):** Veritabanı bağlantısı ve SQL sorgularının çalıştırıldığı katman.

---

## 🚀 Kurulum ve Çalıştırma

1. Projeyi bilgisayarınıza klonlayın:
   ```bash
   git clone [https://github.com/Aysenur-ark/ClubManagementSystem-2.git](https://github.com/Aysenur-ark/ClubManagementSystem-2.git)
