
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author havvanuryanik
 */
public class anaOyunEkrani extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(anaOyunEkrani.class.getName());
    private final String kelimedosya= "/Users/havvanuryanik/Desktop/P2OYUN/txtdosyalar/kelimeler.txt";
    private final String oyundosya = "/Users/havvanuryanik/Desktop/P2OYUN/txtdosyalar/oyunlar.txt";
     private final String logdosya = "/Users/havvanuryanik/Desktop/P2OYUN/txtdosyalar/log.txt";
    private final String resimlerdosya = "/Users/havvanuryanik/Desktop/P2OYUN/resimler/";
    private String secilikelime = "";
    private String kelime = ""; 
    private int yanlisTahminSayisi = 0;
    private final int maksimumhak = 11;
    private int gecenSaniye = 0;
    private Timer oyunTimer;

    /**
     * Creates new form anaOyunEkranı
     */
    public anaOyunEkrani() {
        initComponents();
        gecenSureyiBaslat();
        kelimeSecVeBaslat();
        tabloyuDoldur();
        tabloyuDoldurlog();
        
    }
    private void gecenSureyiBaslat() {
        oyunTimer = new Timer(1000, e -> {
            gecenSaniye++;
            jLabelsure.setText("Geçen Süre: " + gecenSaniye + " saniye");
        });
        oyunTimer.start();
    }
    private void oyunuYenidenBaslat() {
        yanlisTahminSayisi = 0;
        jTextFieldharf.setText("");
        jTextFieldkelimeler.setText("");
        jLabelresim.setIcon(null);
        jLabelresim.setText("Resim");
        
        gecenSureyiBaslat();
        kelimeSecVeBaslat();
        
        JOptionPane.showMessageDialog(this, "Oyun sıfırlandı.", "Yeniden Başlat", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void tabloyuDoldur() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        File dosya = new File(oyundosya);
        if (!dosya.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                String[] parcalar = satir.split("\\|");
                
                if (parcalar.length >= 3) {
                    String sonuc = parcalar[1].replace("Durum:", "").trim();
                    String sure = parcalar[2].replace("Süre:", "").trim();

                    model.addRow(new Object[]{sure, sonuc});
                } 
                else {
                    model.addRow(new Object[]{satir, ""});
                }
            }
        } 
        catch (IOException e){}
        
    }
    
    private void tabloyuDoldurlog() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);

        File dosya = new File(logdosya);
        if (!dosya.exists()) {
            return; 
        }

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                if (satir.contains("[") && satir.contains("]")) {
                    int baslangicIndeksi = satir.indexOf("[") + 1;
                    int bitisIndeksi = satir.indexOf("]");
                    String girisZamani = satir.substring(baslangicIndeksi, bitisIndeksi).trim();
                    model.addRow(new Object[]{girisZamani});
                } 
                else {
                    model.addRow(new Object[]{satir});
                }
            }
        } catch (IOException e) {}
    }
  
    private void kelimeSecVeBaslat() {
        try {
            File dosya = new File(kelimedosya);
            if (!dosya.exists()) {
                return;
            }
            
            Scanner sayiciScanner = new Scanner(dosya);
            int kelimeSayisi = 0;
            while (sayiciScanner.hasNextLine()) {
                String satir = sayiciScanner.nextLine().trim();
               if (!satir.isEmpty()) {
                    kelimeSayisi++;
               }
            }
            sayiciScanner.close();

            String[] kelimeHavuzu = new String[kelimeSayisi];
            Scanner okuyucuScanner = new Scanner(dosya);
            int indeks = 0;
            while (okuyucuScanner.hasNextLine()) {
                String satir = okuyucuScanner.nextLine().trim();
                if (!satir.isEmpty()) {
                    kelimeHavuzu[indeks] = satir.toUpperCase();
                    indeks++;
                }
            }
            okuyucuScanner.close();


            int rastgeleIndeks = (int) (Math.random() * kelimeSayisi);
            kelime = kelimeHavuzu[rastgeleIndeks];

            int harfSayisi = kelime.length();
            jpanelkelimeler.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 5));
            jpanelkelimeler.removeAll();
            
            for (int i = 0; i < harfSayisi; i++) {
                JLabel lbl = new JLabel("*");
                lbl.setName("lbl" + i); 
                jpanelkelimeler.add(lbl);
            }
            
            jpanelkelimeler.revalidate();
            jpanelkelimeler.repaint();


        } 
        catch (Exception e) {}
    }

private void resimGuncelle() {
    int resimNumarasi = yanlisTahminSayisi + 1; 
    String tamResimYolu = resimlerdosya + resimNumarasi + ".jpg";
   
    File resimDosyasi = new File(tamResimYolu);
    if (!resimDosyasi.exists()) {
        jLabelresim.setIcon(null);
        return;
    }
    try {
        javax.swing.ImageIcon ikon = new javax.swing.ImageIcon(tamResimYolu);
        jLabelresim.setIcon(ikon);
        jLabelresim.setText(yanlisTahminSayisi + " / " + maksimumhak);
    } 
    catch (Exception e) {}
}  

private void harfTahminiYap() {
    String girilenMetin = jTextFieldharf.getText();
    jTextFieldharf.setText("");

    if (girilenMetin.length() != 1) {
        JOptionPane.showMessageDialog(this, "Lütfen tek bir harf giriniz:");
        return;
        }
    String tahminHarfi = girilenMetin.toUpperCase();
    String buyukKelime = kelime.toUpperCase();

    boolean bulundu = false;
    for (int i = 0; i < jpanelkelimeler.getComponentCount(); i++) {
       JLabel siradakiLabel = (JLabel) jpanelkelimeler.getComponent(i);
        char kelimeHarfiChar = buyukKelime.charAt(i);
        String kelimeHarfiStr = "" + kelimeHarfiChar; 
        
        if (tahminHarfi.equals(kelimeHarfiStr)) {
            siradakiLabel.setText(tahminHarfi); 
            bulundu = true;}
        }
    
        if (bulundu) {
            System.out.println("var");
            kontrolOyunKazanildiMi();
        } 
        
        else {
            System.out.println("yok");
            yanlisTahminSayisi++;
            resimGuncelle();
            kontrolOyunKaybedildiMi();
        }
    }

    private void kelimeTahminiYap() {
        String girilenKelime = jTextFieldkelimeler.getText();
        jTextFieldkelimeler.setText(""); 

        if (girilenKelime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen bir kelime yazınız:");
            return;
        }
        String tahminKelimeBuyuk = girilenKelime.toUpperCase();
        String sistemdekiKelimeBuyuk = kelime.toUpperCase();
        
        if (sistemdekiKelimeBuyuk.equals(tahminKelimeBuyuk)) {
            for (int i = 0; i < jpanelkelimeler.getComponentCount(); i++) {
                JLabel siradakiLabel = (JLabel) jpanelkelimeler.getComponent(i);

                char harfChar = sistemdekiKelimeBuyuk.charAt(i);
                siradakiLabel.setText("" + harfChar);
            }

            oyunuBitir(true);
        } 
        
        else {
            yanlisTahminSayisi++;
            resimGuncelle();
            kontrolOyunKaybedildiMi();
        }
    }
    
    
    private void kontrolOyunKazanildiMi() {
        boolean hepsiAcildiMu = true;
        for (int i = 0; i < jpanelkelimeler.getComponentCount(); i++) {
            JLabel siradakiLabel = (JLabel) jpanelkelimeler.getComponent(i);
            if (siradakiLabel.getText().equals("*")) {
                hepsiAcildiMu = false;
                break;
            }
        }
        
        if (hepsiAcildiMu) {
            oyunuBitir(true);
        }
    }
    private void kontrolOyunKaybedildiMi() {
        if (yanlisTahminSayisi >= maksimumhak) {
            oyunuBitir(false);
        }
    }
    private void oyunuBitir(boolean kazanildiMu) {
        oyunTimer.stop();
        String sonucMetni = kazanildiMu ? "KAZANDI" : "KAYBETTİ";
        Date suAnkiTarih = new Date(); 

        try {
            FileWriter fw = new FileWriter(oyundosya, true);
            fw.write("[" + suAnkiTarih.toString() + "] Kelime: " + kelime + " | Durum: " + sonucMetni + " | Süre: " + gecenSaniye + " saniye\n");
            fw.close();

            tabloyuDoldur();
            tabloyuDoldurlog();
            } 
        catch (Exception e) {}

       if (kazanildiMu) {
            JOptionPane.showMessageDialog(this, "Bildiniz: " + kelime + "\nHarcanan Süre: " + gecenSaniye + " saniye", "Oyun Kazanıldı", JOptionPane.INFORMATION_MESSAGE);
        } 
        else {
            JOptionPane.showMessageDialog(this, "Tahmin hakkınız bitti. Kelime: " + kelime + "\nHarcanan Süre: " + gecenSaniye + " saniye", "Oyun Kaybedildi", JOptionPane.WARNING_MESSAGE);
            }
        }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtontemizle = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelresim = new javax.swing.JLabel();
        jpanelkelimeler = new javax.swing.JPanel();
        jTextFieldkelimeler = new javax.swing.JTextField();
        jTextFieldharf = new javax.swing.JTextField();
        jButtonharf = new javax.swing.JButton();
        jButtonkelime = new javax.swing.JButton();
        jLabelsure = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Giriş zamanı"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("temizle");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(jButton1)
                .addContainerGap(274, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(jButton1)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Logları Görüntüle", jPanel3);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Süre", "Sonuç"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButtontemizle.setText("Temizle");
        jButtontemizle.addActionListener(this::jButtontemizleActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtontemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(jButtontemizle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Eski Skorları Görüntüle", jPanel4);

        jLabelresim.setText("Resim");

        jpanelkelimeler.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpanelkelimelerLayout = new javax.swing.GroupLayout(jpanelkelimeler);
        jpanelkelimeler.setLayout(jpanelkelimelerLayout);
        jpanelkelimelerLayout.setHorizontalGroup(
            jpanelkelimelerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 419, Short.MAX_VALUE)
        );
        jpanelkelimelerLayout.setVerticalGroup(
            jpanelkelimelerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jTextFieldharf.addActionListener(this::jTextFieldharfActionPerformed);

        jButtonharf.setText("harf kontrol");
        jButtonharf.addActionListener(this::jButtonharfActionPerformed);

        jButtonkelime.setText("kelime kontrol");
        jButtonkelime.addActionListener(this::jButtonkelimeActionPerformed);

        jLabelsure.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanelkelimeler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelresim, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldharf, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonharf))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonkelime)
                            .addComponent(jTextFieldkelimeler, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 409, Short.MAX_VALUE)
                        .addComponent(jLabelsure, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabelsure, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldharf, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldkelimeler, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonharf)
                            .addComponent(jButtonkelime)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabelresim, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jpanelkelimeler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(144, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Oyun Oyna", jPanel2);

        jMenu1.setText("OYUN");
        jMenu1.addActionListener(this::jMenu1ActionPerformed);

        jMenuItem1.setText("Başla ");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Yeniden başlat");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Çıkış");
        jMenuItem3.addActionListener(this::jMenuItem3ActionPerformed);
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(338, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldharfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldharfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldharfActionPerformed

    private void jButtonharfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonharfActionPerformed
        // TODO add your handling code here:
        harfTahminiYap();
    }//GEN-LAST:event_jButtonharfActionPerformed

    private void jButtonkelimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonkelimeActionPerformed
        // TODO add your handling code here:
        kelimeTahminiYap();
    }//GEN-LAST:event_jButtonkelimeActionPerformed

    private void jButtontemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtontemizleActionPerformed
        // TODO add your handling code here:
        String girilenSifre = JOptionPane.showInputDialog(this, "Şifre Giriniz:", "Güvenlik Onayı", JOptionPane.WARNING_MESSAGE);
        String ADMIN_SIFRE="1234";
        if (girilenSifre == null) {
            return; 
        }

        if (girilenSifre.equals(ADMIN_SIFRE)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(oyundosya, false))) {
                bw.write(""); 
            } 
            catch (IOException e) {}

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            JOptionPane.showMessageDialog(this, "Kayıtlar Temizlendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            
        } 
        else {

            JOptionPane.showMessageDialog(this, "Hatalı şifre!", "Erişim Reddedildi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtontemizleActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String girilenSifre = JOptionPane.showInputDialog(this, "şifre Giriniz:", "Güvenlik Onayı", JOptionPane.WARNING_MESSAGE);
        String ADMIN_SIFRE="1234";
        
        if (girilenSifre == null) {
            return; 
        }

        if (girilenSifre.equals(ADMIN_SIFRE)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logdosya, false))) {
                bw.write(""); 
            } 
            catch (IOException e) {}

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            JOptionPane.showMessageDialog(this, "Kayıtlar Temizlendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            
        } else {

            JOptionPane.showMessageDialog(this, "Hatalı şifre!", "Erişim Reddedildi", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
 
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(oyunTimer != null && !oyunTimer.isRunning()) {
            gecenSaniye = 0;
            yanlisTahminSayisi = 0;
            kelimeSecVeBaslat();
            oyunTimer.start();
            JOptionPane.showMessageDialog(this, "Yeni oyun başladı.");
        } 
        else {
            JOptionPane.showMessageDialog(this, "Aktif bir oyun devam ediyor.");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        if (oyunTimer != null) {
            oyunTimer.stop();
        }
        gecenSaniye = 0;
        yanlisTahminSayisi = 0;
        jLabelsure.setText("Geçen Süre: 0 saniye");
        
        kelimeSecVeBaslat();
        
        if (oyunTimer != null) {
            oyunTimer.start();
        }
        JOptionPane.showMessageDialog(this, "Oyun yeniden başlatıldı.");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        int onay = JOptionPane.showConfirmDialog(this, "Çıkış yapmak istediğinize emin misiniz?", "Çıkış Onayı", JOptionPane.YES_NO_OPTION);
        if (onay == JOptionPane.YES_OPTION) {
            if (oyunTimer != null) {
                oyunTimer.stop();
            }
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new anaOyunEkrani().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonharf;
    private javax.swing.JButton jButtonkelime;
    private javax.swing.JButton jButtontemizle;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabelresim;
    private javax.swing.JLabel jLabelsure;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextFieldharf;
    private javax.swing.JTextField jTextFieldkelimeler;
    private javax.swing.JPanel jpanelkelimeler;
    // End of variables declaration//GEN-END:variables
}
