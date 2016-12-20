package forms;

import enumerations.Manufacturer;
import enumerations.TransmissionType;
import enumerations.VehicleType;
import java.awt.Color;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import mainClasses.Rental;
import mainClasses.Staff;
import mainClasses.TransportSystem;
import mainClasses.Vehicle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Liam
 */
public class RentVehicle extends javax.swing.JFrame {

    /**
     * Creates new form RentReturnCar
     */
    Staff currentStaff;
    Vehicle currentVehicle;
    ArrayList<Staff> staffList;
    ArrayList<Vehicle> availableVehiclesList = new ArrayList<>();
    DefaultListModel<String> defaultModel = new DefaultListModel<>();

    public RentVehicle() {
        initComponents();
        staffList = TransportSystem.getInstance().getStaffList();
        for (Vehicle tempVehicle : TransportSystem.getInstance().getVehicleList()) {
            if (tempVehicle.isAvailable() == true) {
                availableVehiclesList.add(tempVehicle);
                defaultModel.addElement(tempVehicle.getLicensePlate());
            }
        }
        lstVehicleList.setModel(defaultModel);
        lblStaffName.setBackground(Color.red);
        lblStaffName.setOpaque(true);
        lblVehicle.setBackground(Color.red);
        lblVehicle.setOpaque(true);
        cmbVehicleFilter.setSelectedIndex(0);
        btnRentVehicle.setEnabled(false);
    }

    private void rentVehicle() {
        if (currentStaff.getStaffID().equals(TransportSystem.getInstance().getCurrentAdmin().getStaffID())) {
            JOptionPane.showMessageDialog(this, "You can not rent a car to yourself!");

        } else {
            long spinnerValue = (long) spnDuration.getValue();
            Duration duration = Duration.ofDays(spinnerValue);
            if (currentStaff != null && currentVehicle != null) {
                Rental newRental = new Rental(TransportSystem.getInstance().getCurrentAdmin(),
                        currentStaff, currentVehicle, LocalDate.now(), duration);
                this.dispose();
            }
        }
    }

    private void searchStaff() {
        for (Staff tempStaff : staffList) {
            System.out.println(txtStaffID.getText() + " " + tempStaff.getStaffID());
            if (txtStaffID.getText().equals(tempStaff.getStaffID())) {
                currentStaff = tempStaff;
                lblStaffName.setText(currentStaff.getFirstName() + " " + currentStaff.getLastName());
                lblStaffName.setBackground(Color.green);
                if (currentVehicle != null && currentStaff != null) {
                    btnRentVehicle.setEnabled(true);
                }
                return;
            }
        }
        lblStaffName.setText("");
        lblStaffName.setBackground(Color.red);
        btnRentVehicle.setEnabled(false);
        currentStaff = null;
    }

    private void searchVehicle() {
        int index = lstVehicleList.getSelectedIndex();
        if (index != -1) {

            for (Vehicle tempVehicle : availableVehiclesList) {
                if (tempVehicle.getLicensePlate() == lstVehicleList.getModel().getElementAt(index)) {
                    currentVehicle = tempVehicle;
                    lblVehicle.setText(tempVehicle.getLicensePlate());
                    lblVehicle.setBackground(Color.green);
                    if (currentVehicle != null && currentStaff != null) {
                        btnRentVehicle.setEnabled(true);
                    }
                    return;
                }

            }
            lblVehicle.setText("");
            lblVehicle.setBackground(Color.red);
            btnRentVehicle.setEnabled(false);
            currentVehicle = null;
        }
    }

    private void filterList() {
        if (cmbFilter.getSelectedIndex() != 0 && cmbFilter.getSelectedIndex() != -1) {
            String input = (String) cmbFilter.getSelectedItem();
            ArrayList<Vehicle> tempVehicleList = new ArrayList<>();
            if ((String) cmbVehicleFilter.getSelectedItem() == "Vehicle Type") {
                for (Vehicle tempVehicle : availableVehiclesList) {
                    if (tempVehicle.getVehicleType() == VehicleType.valueOf(input)) {
                        tempVehicleList.add(tempVehicle);
                    }
                }
            } else if ((String) cmbVehicleFilter.getSelectedItem() == "Transmission") {
                for (Vehicle tempVehicle : availableVehiclesList) {
                    if (tempVehicle.getTransmission() == TransmissionType.valueOf(input)) {
                        tempVehicleList.add(tempVehicle);
                    }
                }
            } else if ((String) cmbVehicleFilter.getSelectedItem() == "Manufacturer") {
                for (Vehicle tempVehicle : availableVehiclesList) {
                    if (tempVehicle.getManufacturer() == Manufacturer.valueOf(input)) {
                        tempVehicleList.add(tempVehicle);
                    }
                }
            } else if ((String) cmbVehicleFilter.getSelectedItem() == "Seats") {
                for (Vehicle tempVehicle : availableVehiclesList) {
                    {
                        if (tempVehicle.getSeats() == Integer.parseInt(input)) {
                            tempVehicleList.add(tempVehicle);
                        }
                    }
                }
            }
            DefaultListModel<String> model = new DefaultListModel<>();
            for (Vehicle tempVehicle : tempVehicleList) {
                model.addElement(tempVehicle.getLicensePlate());
            }
            lstVehicleList.setSelectedIndex(-1);
            lstVehicleList.setModel(model);
        } else {
            lstVehicleList.setModel(defaultModel);
        }
    }

    private void changeFilter() {
        cmbFilter.removeAllItems();
        cmbFilter.addItem("No Filter");
        if ((String) cmbVehicleFilter.getSelectedItem() == "Vehicle Type") {
            for (VehicleType currentType : VehicleType.values()) {
                cmbFilter.addItem(currentType.toString());
            }
        } else if ((String) cmbVehicleFilter.getSelectedItem() == "Transmission") {
            for (TransmissionType currentType : TransmissionType.values()) {
                cmbFilter.addItem(currentType.toString());
            }
        } else if ((String) cmbVehicleFilter.getSelectedItem() == "Manufacturer") {
            for (Manufacturer currentManufacturer : Manufacturer.values()) {
                cmbFilter.addItem(currentManufacturer.toString());
            }
        } else if ((String) cmbVehicleFilter.getSelectedItem() == "Seats") {
            ArrayList<Integer> numSeats = new ArrayList<>();
            for (Vehicle currentVehicle : availableVehiclesList) {
                {
                    if (!numSeats.contains(currentVehicle.getSeats())) {
                        numSeats.add(currentVehicle.getSeats());
                    }
                }
                Collections.sort(numSeats);
            }
            for (Integer seats : numSeats) {
                cmbFilter.addItem(seats.toString());
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        lstVehicleList = new javax.swing.JList<>();
        lblAvailableCars = new javax.swing.JLabel();
        cmbVehicleFilter = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtStaffID = new javax.swing.JTextField();
        btnRentVehicle = new javax.swing.JButton();
        lblStaffName = new javax.swing.JLabel();
        spnDuration = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblVehicle = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        cmbFilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lstVehicleList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstVehicleList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstVehicleListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstVehicleList);

        lblAvailableCars.setText("Available Cars:");

        cmbVehicleFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vehicle Type", "Transmission", "Manufacturer", "Seats" }));
        cmbVehicleFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVehicleFilterActionPerformed(evt);
            }
        });

        jLabel2.setText("Enter StaffID:");

        txtStaffID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStaffIDKeyReleased(evt);
            }
        });

        btnRentVehicle.setText("Rent Vehicle");
        btnRentVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentVehicleActionPerformed(evt);
            }
        });

        spnDuration.setModel(new javax.swing.SpinnerNumberModel(1L, 1L, null, 1L));

        jLabel1.setText("Duration (Days) :");

        jLabel3.setText("Current Staff:");

        jLabel4.setText("Current Vehicle:");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cmbFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAvailableCars)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtStaffID, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(spnDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(cmbVehicleFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblStaffName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(btnRentVehicle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addGap(110, 110, 110))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtStaffID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(lblAvailableCars)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStaffName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbVehicleFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnRentVehicle)
                        .addContainerGap(33, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addGap(19, 19, 19))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtStaffIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStaffIDKeyReleased
        searchStaff();
    }//GEN-LAST:event_txtStaffIDKeyReleased

    private void lstVehicleListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstVehicleListValueChanged
        searchVehicle();
    }//GEN-LAST:event_lstVehicleListValueChanged

    private void btnRentVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentVehicleActionPerformed
        rentVehicle();
    }//GEN-LAST:event_btnRentVehicleActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cmbVehicleFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbVehicleFilterActionPerformed
        changeFilter();
    }//GEN-LAST:event_cmbVehicleFilterActionPerformed

    private void cmbFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterActionPerformed
        filterList();
    }//GEN-LAST:event_cmbFilterActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RentVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RentVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RentVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RentVehicle.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RentVehicle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRentVehicle;
    private javax.swing.JComboBox<String> cmbFilter;
    private javax.swing.JComboBox<String> cmbVehicleFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvailableCars;
    private javax.swing.JLabel lblStaffName;
    private javax.swing.JLabel lblVehicle;
    private javax.swing.JList<String> lstVehicleList;
    private javax.swing.JSpinner spnDuration;
    private javax.swing.JTextField txtStaffID;
    // End of variables declaration//GEN-END:variables
}
