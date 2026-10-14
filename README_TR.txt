ZORYAK AI 14.0 ANDROID UYGULAMASI - KAYNAK PROJESİ
==================================================

ÖNEMLİ: Bu ZIP henüz hazır bir APK dosyası değildir; Android Studio'da APK üretmek için hazırlanmış Android projesidir.
Bu ortamda Android SDK/Gradle bulunmadığı için derlenmiş APK oluşturup test edemedim.

NE YAPAR?
- Android uygulaması gibi açılan WebView arayüzü
- Bilgisayardaki Zoryak AI sunucu adresini ilk açılışta sorma ve hatırlama
- Web arayüzünün dosya seçici desteği
- Web sayfası isterse mikrofon/kamera izinlerini Android'den isteme
- Geri tuşu ve bağlantı durumu

MİMARİ SINIRLAMA:
Bu APK istemci uygulamasıdır. Zoryak'ın Python sunucusu ve Ollama modeli Windows bilgisayarında çalışmaya devam eder.
Bilgisayar ve telefon aynı Wi-Fi ağına bağlı olmalıdır. Bilgisayar açık kalmalı ve Windows Güvenlik Duvarı'nda
özel ağda 8013/TCP bağlantısına izin verilmelidir. Uygulamada sunucu adresi olarak bilgisayarın siyah penceresinde
çıkan "Android / aynı Wi-Fi" adresini kullanın. Örnek: http://192.168.1.35:8013

APK NASIL OLUŞTURULUR?
1. Bilgisayara Android Studio kurun: https://developer.android.com/studio
2. Android Studio'yu açıp "Open" seçin ve bu ZIP'ten çıkan ZoryakAI_14_Android klasörünü açın.
3. Gradle senkronizasyonunun bitmesini bekleyin. İstenirse Android SDK Platform 35 ve uyumlu Android Build Tools'u kurun.
4. Menüden Build > Build Bundle(s) / APK(s) > Build APK(s) seçin.
5. İşlem tamamlandığında çıkan bildiride "locate" seçeneği APK'nın bulunduğu klasörü açar.
   Genellikle: app/build/outputs/apk/debug/app-debug.apk
6. APK'yı telefona gönderip açın. Android, bilinmeyen uygulama yüklemesine izin istemesi halinde yalnızca kendi oluşturduğunuz APK için izin verin.

İLK KULLANIM
1. Windows bilgisayarında Zoryak AI 14.0'ın start.bat dosyasını çalıştırın.
2. Siyah penceredeki Android / aynı Wi-Fi adresini not edin.
3. Telefonda Zoryak AI uygulamasını açıp adresi girin.
4. Telefon ve bilgisayar aynı Wi-Fi'da olmalı; VPN/istemci izolasyonu bağlantıyı engelleyebilir.

GÜVENLİK NOTU
Bu uygulama yerel ağda HTTP kullanabilir çünkü mevcut yerel sunucu HTTP'dir. Bunu internete açık şekilde yönlendirmeyin.
Gerçek e-posta doğrulaması ve görsel üretimi gibi sunucu özellikleri ayrıca doğru şekilde yapılandırılmış olmalıdır.
