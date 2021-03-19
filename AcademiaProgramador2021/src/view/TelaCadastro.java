/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.JOptionPane;

public class TelaCadastro extends javax.swing.JFrame implements Serializable {

    ArrayList<Chamadas> listaChamadas;
    ArrayList<Equipamentos> listaEquipamentos;
    String modo;

    public TelaCadastro() {
        try {
            initComponents();

            MaskFormatter maskDataC = new MaskFormatter("##/##/####");
            MaskFormatter maskDataE = new MaskFormatter("##/##/####");
            maskDataC.install(text_chamada_data);
            maskDataE.install(text_equip_data);

            listaEquipamentos = new ArrayList<>();
            listaChamadas = new ArrayList<>();

            listaEquipamentos = desserializarEquipamentos();
            listaChamadas = desserializarChamadas();
                        
            LoadTableEquip();
            LoadTableChamada();

            modo = "Navegar";
            ManipulaInterfaceEquip();
            ManipulaInterfaceChamada();
        } catch (ParseException ex) {
            Logger.getLogger(TelaCadastro.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void serializarChamadas(ArrayList<Chamadas> listaChamadas) {
        try {
            FileOutputStream arquivo = new FileOutputStream("listaChamadas.ser");
            ObjectOutputStream out = new ObjectOutputStream(arquivo);

            out.writeObject(listaChamadas);
            out.close();
            arquivo.close();
        } catch (Exception e) {
            System.out.println("Erro na serialização!");
        }
    }

    public ArrayList<Chamadas> desserializarChamadas() {
        try {
            FileInputStream arquivo = new FileInputStream("listaChamadas.ser");
            ObjectInputStream inp = new ObjectInputStream(arquivo);

            ArrayList<Chamadas> listaChamadas = (ArrayList<Chamadas>) inp.readObject();
            inp.close();
            arquivo.close();
            System.out.println(listaChamadas.toString());
            return listaChamadas;
        } catch (Exception e) {
            System.out.println("Erro na Desserialização!");
            return listaChamadas;
        }
    }

    public void serializarEquipamentos(ArrayList<Equipamentos> inventarioEquipamentos) {
        try {
            FileOutputStream arquivo = new FileOutputStream("inventarioEquipamentos.ser");
            ObjectOutputStream out = new ObjectOutputStream(arquivo);

            out.writeObject(inventarioEquipamentos);
            out.close();
            arquivo.close();
        } catch (Exception e) {
            System.out.println("Erro na serialização!");
        }
    }

    public ArrayList<Equipamentos> desserializarEquipamentos() {
        try {
            FileInputStream arquivo = new FileInputStream("inventarioEquipamentos.ser");
            ObjectInputStream inp = new ObjectInputStream(arquivo);

            ArrayList<Equipamentos> listaEquipamentos = (ArrayList<Equipamentos>) inp.readObject();
            inp.close();
            arquivo.close();
            System.out.println(listaEquipamentos.toString());
            return listaEquipamentos;
        } catch (Exception e) {
            System.out.println("Erro na Desserialização!");
            return listaEquipamentos;
        }
    }

    public void LoadTableEquip() {

        DefaultTableModel modeloE = new DefaultTableModel(new Object[]{"Nome", "Número de Série", "Fabricante"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        for (int i = 0; i < listaEquipamentos.size(); i++) {

            Object linha[] = new Object[]{listaEquipamentos.get(i).getNomePeça(), listaEquipamentos.get(i).getNumeroSerie(), listaEquipamentos.get(i).getNomeFabricante()};
            modeloE.addRow(linha);

        }

        tbl_equip.setModel(modeloE);

    }

    public void LoadTableChamada() {

        Data hoje = new Data();
        Timestamp temp;
        long ms;

        DefaultTableModel modeloC = new DefaultTableModel(new Object[]{"Título do Chamado", "Equipamento", "Data de Abertura", "Dias em Aberto"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        try {

            for (int i = 0; i < listaChamadas.size(); i++) {

                temp = listaChamadas.get(i).getData().getTimestamp();

                ms = ((hoje.getTimestamp().getTime() - temp.getTime())) / 86400000;

                Object linha[] = new Object[]{listaChamadas.get(i).getTitulo(), listaChamadas.get(i).getEquipamento(), listaChamadas.get(i).getData().getDataString(), ms};
                modeloC.addRow(linha);

            }
            tbl_chamada.setModel(modeloC);
        } catch (Exception i) {

            JOptionPane.showMessageDialog(null, "Dados Inválidos!", "ERRO", JOptionPane.ERROR_MESSAGE);

        }

    }

    public void LimpaCamposChamada() {

        text_chamada_data.setText("");
        text_chamada_desc.setText("");
        text_chamada_titulo.setText("");

    }

    public void LimpaCamposEquip() {

        text_equip_data.setText("");
        text_equip_nome.setText("");
        text_equip_fabricante.setText("");
        text_equip_preco.setText("");
        text_equip_serie.setText("");

    }

    public void ManipulaInterfaceChamada() {

        switch (modo) {

            case "Navegar":
                bt_chamada_salvar.setEnabled(false);
                bt_chamada_cancelar.setEnabled(false);
                bt_chamada_novo.setEnabled(true);
                bt_chamada_editar.setEnabled(false);
                bt_chamada_excluir.setEnabled(false);
                text_chamada_data.setEnabled(false);
                text_chamada_desc.setEnabled(false);
                text_chamada_titulo.setEnabled(false);
                combo_chamada_equipamento.setEnabled(false);

                break;

            case "Novo":
                bt_chamada_salvar.setEnabled(true);
                bt_chamada_cancelar.setEnabled(true);
                bt_chamada_novo.setEnabled(false);
                bt_chamada_editar.setEnabled(false);
                bt_chamada_excluir.setEnabled(false);
                text_chamada_data.setEnabled(true);
                text_chamada_desc.setEnabled(true);
                text_chamada_titulo.setEnabled(true);
                combo_chamada_equipamento.setEnabled(true);

                break;
            case "Editar":
                bt_chamada_salvar.setEnabled(true);
                bt_chamada_cancelar.setEnabled(true);
                bt_chamada_novo.setEnabled(true);
                bt_chamada_editar.setEnabled(false);
                bt_chamada_excluir.setEnabled(false);
                text_chamada_data.setEnabled(true);
                text_chamada_desc.setEnabled(true);
                text_chamada_titulo.setEnabled(true);
                combo_chamada_equipamento.setEnabled(true);

                break;
            case "Excluir":
                bt_chamada_salvar.setEnabled(false);
                bt_chamada_cancelar.setEnabled(false);
                bt_chamada_novo.setEnabled(true);
                bt_chamada_editar.setEnabled(false);
                bt_chamada_excluir.setEnabled(false);
                text_chamada_data.setEnabled(false);
                text_chamada_desc.setEnabled(false);
                text_chamada_titulo.setEnabled(false);
                combo_chamada_equipamento.setEnabled(false);

                break;
            case "Seleção":
                bt_chamada_salvar.setEnabled(false);
                bt_chamada_cancelar.setEnabled(false);
                bt_chamada_novo.setEnabled(true);
                bt_chamada_editar.setEnabled(true);
                bt_chamada_excluir.setEnabled(true);
                text_chamada_data.setEnabled(false);
                text_chamada_desc.setEnabled(false);
                text_chamada_titulo.setEnabled(false);
                combo_chamada_equipamento.setEnabled(false);

                break;

        }

    }

    public void ManipulaInterfaceEquip() {

        switch (modo) {

            case "Navegar":
                bt_equip_salvar.setEnabled(false);
                bt_equip_cancelar.setEnabled(false);
                bt_equip_novo.setEnabled(true);
                bt_equip_editar.setEnabled(false);
                bt_equip_excluir.setEnabled(false);
                text_equip_data.setEnabled(false);
                text_equip_fabricante.setEnabled(false);
                text_equip_nome.setEnabled(false);
                text_equip_preco.setEnabled(false);
                text_equip_serie.setEnabled(false);

                break;

            case "Novo":
                bt_equip_salvar.setEnabled(true);
                bt_equip_cancelar.setEnabled(true);
                bt_equip_novo.setEnabled(false);
                bt_equip_editar.setEnabled(false);
                bt_equip_excluir.setEnabled(false);
                text_equip_data.setEnabled(true);
                text_equip_fabricante.setEnabled(true);
                text_equip_nome.setEnabled(true);
                text_equip_preco.setEnabled(true);
                text_equip_serie.setEnabled(true);

                break;
            case "Editar":
                bt_equip_salvar.setEnabled(true);
                bt_equip_cancelar.setEnabled(true);
                bt_equip_novo.setEnabled(true);
                bt_equip_editar.setEnabled(false);
                bt_equip_excluir.setEnabled(false);
                text_equip_data.setEnabled(true);
                text_equip_fabricante.setEnabled(true);
                text_equip_nome.setEnabled(true);
                text_equip_preco.setEnabled(true);
                text_equip_serie.setEnabled(true);

                break;
            case "Excluir":
                bt_equip_salvar.setEnabled(false);
                bt_equip_cancelar.setEnabled(false);
                bt_equip_novo.setEnabled(true);
                bt_equip_editar.setEnabled(false);
                bt_equip_excluir.setEnabled(false);
                text_equip_data.setEnabled(false);
                text_equip_fabricante.setEnabled(false);
                text_equip_nome.setEnabled(false);
                text_equip_preco.setEnabled(false);
                text_equip_serie.setEnabled(false);

                break;
            case "Seleção":
                bt_equip_salvar.setEnabled(false);
                bt_equip_cancelar.setEnabled(false);
                bt_equip_novo.setEnabled(true);
                bt_equip_editar.setEnabled(true);
                bt_equip_excluir.setEnabled(true);
                text_equip_data.setEnabled(false);
                text_equip_fabricante.setEnabled(false);
                text_equip_nome.setEnabled(false);
                text_equip_preco.setEnabled(false);
                text_equip_serie.setEnabled(false);

                break;

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_equip = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        text_equip_nome = new javax.swing.JTextField();
        text_equip_preco = new javax.swing.JTextField();
        text_equip_serie = new javax.swing.JTextField();
        text_equip_fabricante = new javax.swing.JTextField();
        bt_equip_salvar = new javax.swing.JButton();
        bt_equip_cancelar = new javax.swing.JButton();
        text_equip_data = new javax.swing.JFormattedTextField();
        bt_equip_novo = new javax.swing.JButton();
        bt_equip_editar = new javax.swing.JButton();
        bt_equip_excluir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_chamada = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        text_chamada_titulo = new javax.swing.JTextField();
        text_chamada_desc = new javax.swing.JTextField();
        bt_chamada_salvar = new javax.swing.JButton();
        bt_chamada_cancelar = new javax.swing.JButton();
        text_chamada_data = new javax.swing.JFormattedTextField();
        combo_chamada_equipamento = new javax.swing.JComboBox<>();
        bt_chamada_novo = new javax.swing.JButton();
        bt_chamada_editar = new javax.swing.JButton();
        bt_chamada_excluir = new javax.swing.JButton();

        jFormattedTextField1.setText("jFormattedTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        tbl_equip.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Número de Série", "Fabricante"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_equip.getTableHeader().setReorderingAllowed(false);
        tbl_equip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_equipMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_equip);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações"));

        jLabel1.setText("Nome :");

        jLabel2.setText("Preço de aquisição :");

        jLabel3.setText("Número de série :");

        jLabel4.setText("Data de Fabricação :");

        jLabel5.setText("Fabricante :");

        bt_equip_salvar.setText("Salvar");
        bt_equip_salvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_equip_salvarMouseClicked(evt);
            }
        });

        bt_equip_cancelar.setText("Cancelar");
        bt_equip_cancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_equip_cancelarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_equip_nome)
                            .addComponent(text_equip_preco)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(bt_equip_salvar, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bt_equip_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(text_equip_serie)
                            .addComponent(text_equip_fabricante)
                            .addComponent(text_equip_data))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(text_equip_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(text_equip_preco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(text_equip_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(text_equip_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(text_equip_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_equip_salvar)
                    .addComponent(bt_equip_cancelar))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        bt_equip_novo.setText("Novo");
        bt_equip_novo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_equip_novoMouseClicked(evt);
            }
        });

        bt_equip_editar.setText("Editar");
        bt_equip_editar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_equip_editarMouseClicked(evt);
            }
        });

        bt_equip_excluir.setText("Excluir");
        bt_equip_excluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_equip_excluirMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bt_equip_novo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_equip_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_equip_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_equip_novo)
                    .addComponent(bt_equip_editar)
                    .addComponent(bt_equip_excluir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );

        jTabbedPane1.addTab("Equipamentos", jPanel1);

        tbl_chamada.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Título da chamada", "Equipamento", "Data de Abertura", "Dias em Aberto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_chamada.getTableHeader().setReorderingAllowed(false);
        tbl_chamada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_chamadaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_chamada);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações"));

        jLabel6.setText("Título da Chamada :");

        jLabel7.setText("Descrição da Chamada :");

        jLabel8.setText("Data de Abertura :");

        jLabel9.setText("Equipamento :");

        bt_chamada_salvar.setText("Salvar");
        bt_chamada_salvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_chamada_salvarMouseClicked(evt);
            }
        });

        bt_chamada_cancelar.setText("Cancelar");
        bt_chamada_cancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_chamada_cancelarMouseClicked(evt);
            }
        });

        combo_chamada_equipamento.setMaximumRowCount(10);
        combo_chamada_equipamento.setToolTipText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_chamada_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(text_chamada_desc)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(bt_chamada_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_chamada_data)
                            .addComponent(combo_chamada_equipamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(bt_chamada_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(text_chamada_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(text_chamada_desc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(text_chamada_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_chamada_salvar))
                    .addComponent(combo_chamada_equipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bt_chamada_cancelar)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        bt_chamada_novo.setText("Novo");
        bt_chamada_novo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_chamada_novoMouseClicked(evt);
            }
        });

        bt_chamada_editar.setText("Editar");
        bt_chamada_editar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_chamada_editarMouseClicked(evt);
            }
        });

        bt_chamada_excluir.setText("Excluir");
        bt_chamada_excluir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bt_chamada_excluirMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(bt_chamada_novo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_chamada_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_chamada_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_chamada_novo)
                    .addComponent(bt_chamada_editar)
                    .addComponent(bt_chamada_excluir))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );

        jTabbedPane1.addTab("Chamadas", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_equip_novoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_equip_novoMouseClicked

        modo = "Novo";
        ManipulaInterfaceEquip();
        LimpaCamposEquip();


    }//GEN-LAST:event_bt_equip_novoMouseClicked

    private void bt_equip_cancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_equip_cancelarMouseClicked

        modo = "Navegar";
        ManipulaInterfaceEquip();
        LimpaCamposEquip();
    }//GEN-LAST:event_bt_equip_cancelarMouseClicked

    private void bt_equip_salvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_equip_salvarMouseClicked

        Boolean testador = false;

        try {
            double preco = Double.parseDouble(text_equip_preco.getText());
            int serie = Integer.parseInt(text_equip_serie.getText());

            for (int j = 0; j < listaEquipamentos.size(); j++) {

                if (serie == listaEquipamentos.get(j).getNumeroSerie()) {

                    testador = true;
                }

            }

            if (testador == true) {

                JOptionPane.showMessageDialog(null, "NÚMERO DE SÉRIE JÁ CADASTRADO", "ERRO ", JOptionPane.ERROR_MESSAGE);

            } else if (text_equip_nome.getText().length() < 6) {

                JOptionPane.showMessageDialog(null, "O NOME NECESSITA DE AO MENOS 6 CARACTERES!", "ERRO ", JOptionPane.ERROR_MESSAGE);

            } else if (text_equip_nome.getText().equals("")
                    || text_equip_fabricante.getText().equals("")
                    || text_equip_data.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "VERIFIQUE OS DADOS INSERIDOS!", "ERRO ", JOptionPane.ERROR_MESSAGE);

            } else {

                if (modo.equals("Novo")) {

                    Equipamentos p = new Equipamentos(text_equip_nome.getText(),
                            text_equip_fabricante.getText(),
                            preco,
                            serie,
                            text_equip_data.getText());
                    listaEquipamentos.add(p);
                } else if (modo.equals("Editar")) {

                    int i = tbl_equip.getSelectedRow();
                    listaEquipamentos.get(i).setDataAquisicao(text_equip_data.getText());
                    listaEquipamentos.get(i).setNomeFabricante(text_equip_fabricante.getText());
                    listaEquipamentos.get(i).setNomePeça(text_equip_nome.getText());
                    listaEquipamentos.get(i).setNumeroSerie(serie);
                    listaEquipamentos.get(i).setPrecoAquisicao(preco);

                }
                serializarEquipamentos(listaEquipamentos);
            }
        } catch (Exception E) {

            JOptionPane.showMessageDialog(null, "VERIFIQUE OS DADOS INSERIDOS!", "ERRO ", JOptionPane.ERROR_MESSAGE);

        }

        LoadTableEquip();
        modo = "Navegar";
        ManipulaInterfaceEquip();
        LimpaCamposEquip();

    }//GEN-LAST:event_bt_equip_salvarMouseClicked

    private void tbl_equipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_equipMouseClicked

        int i = tbl_equip.getSelectedRow();

        if (i >= 0 && i < listaEquipamentos.size()) {

            Equipamentos p = listaEquipamentos.get(i);

            text_equip_nome.setText(p.getNomePeça());
            text_equip_data.setText(p.getDataAquisicao());
            text_equip_fabricante.setText(p.getNomeFabricante());
            text_equip_preco.setText(String.valueOf(p.getPrecoAquisicao()));
            text_equip_serie.setText(String.valueOf(p.getNumeroSerie()));

            modo = "Seleção";
            ManipulaInterfaceEquip();

        }


    }//GEN-LAST:event_tbl_equipMouseClicked

    private void bt_equip_editarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_equip_editarMouseClicked

        modo = "Editar";
        ManipulaInterfaceEquip();

    }//GEN-LAST:event_bt_equip_editarMouseClicked

    private void bt_equip_excluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_equip_excluirMouseClicked

        int i = tbl_equip.getSelectedRow();
        System.out.println(i);

        if (i >=0 && i < listaEquipamentos.size()) {

            listaEquipamentos.remove(i);

        }

        combo_chamada_equipamento.remove(i);
        serializarEquipamentos(listaEquipamentos);
        LoadTableEquip();
        modo = "Navegar";
        LimpaCamposEquip();
        ManipulaInterfaceEquip();


    }//GEN-LAST:event_bt_equip_excluirMouseClicked

    private void tbl_chamadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_chamadaMouseClicked

        int i = tbl_chamada.getSelectedRow();

        if (i >= 0 && i < listaChamadas.size()) {

            Chamadas c = listaChamadas.get(i);

            text_chamada_titulo.setText(c.getTitulo());
            text_chamada_desc.setText(c.getDescricao());
            text_chamada_data.setText(String.valueOf(c.getData().getDataString()));

            modo = "Seleção";
            ManipulaInterfaceChamada();

        }

    }//GEN-LAST:event_tbl_chamadaMouseClicked

    private void bt_chamada_salvarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_chamada_salvarMouseClicked

        try {
            String auxtemp = text_chamada_data.getText();
            Data temp;
            temp = new Data(auxtemp, Data.BarraSemHora);

            if (text_chamada_titulo.getText().equals("")
                    || text_chamada_desc.getText().equals("")
                    || text_chamada_data.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "VERIFIQUE OS DADOS INSERIDOS!", "ERRO ", JOptionPane.ERROR_MESSAGE);

            } else {

                if (modo.equals("Novo")) {

                    Chamadas c = new Chamadas(text_chamada_titulo.getText(),
                            text_chamada_desc.getText(),
                            combo_chamada_equipamento.getSelectedItem().toString(),
                            temp);

                    listaChamadas.add(c);
                } else if (modo.equals("Editar")) {

                    int i = tbl_chamada.getSelectedRow();
                    listaChamadas.get(i).setTitulo(text_chamada_titulo.getText());
                    listaChamadas.get(i).setDescricao(text_chamada_desc.getText());
                    listaChamadas.get(i).setData(temp);

                }

                serializarChamadas(listaChamadas);

            }
        } catch (Exception E) {

            JOptionPane.showMessageDialog(null, "VERIFIQUE OS DADOS INSERIDOS!", "ERRO ", JOptionPane.ERROR_MESSAGE);

        }

        LoadTableChamada();

        modo = "Navegar";
        ManipulaInterfaceChamada();
        LimpaCamposChamada();


    }//GEN-LAST:event_bt_chamada_salvarMouseClicked

    private void bt_chamada_cancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_chamada_cancelarMouseClicked

        modo = "Navegar";
        ManipulaInterfaceChamada();
        LimpaCamposChamada();
    }//GEN-LAST:event_bt_chamada_cancelarMouseClicked

    private void bt_chamada_novoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_chamada_novoMouseClicked

        modo = "Novo";
        ManipulaInterfaceChamada();
        LimpaCamposChamada();
    }//GEN-LAST:event_bt_chamada_novoMouseClicked

    private void bt_chamada_editarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_chamada_editarMouseClicked

        modo = "Editar";
        ManipulaInterfaceChamada();

    }//GEN-LAST:event_bt_chamada_editarMouseClicked

    private void bt_chamada_excluirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bt_chamada_excluirMouseClicked

        int i = tbl_chamada.getSelectedRow();

        if (i >= 0 && i < listaChamadas.size()) {

            listaChamadas.remove(i);

        }

        serializarChamadas(listaChamadas);
        LoadTableChamada();
        modo = "Navegar";
        LimpaCamposChamada();
        ManipulaInterfaceChamada();

    }//GEN-LAST:event_bt_chamada_excluirMouseClicked

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked

        combo_chamada_equipamento.removeAllItems();

        for (Equipamentos u : listaEquipamentos) {

            combo_chamada_equipamento.addItem(u.getNomePeça());

        }


    }//GEN-LAST:event_jTabbedPane1MouseClicked

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
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_chamada_cancelar;
    private javax.swing.JButton bt_chamada_editar;
    private javax.swing.JButton bt_chamada_excluir;
    private javax.swing.JButton bt_chamada_novo;
    private javax.swing.JButton bt_chamada_salvar;
    private javax.swing.JButton bt_equip_cancelar;
    private javax.swing.JButton bt_equip_editar;
    private javax.swing.JButton bt_equip_excluir;
    private javax.swing.JButton bt_equip_novo;
    private javax.swing.JButton bt_equip_salvar;
    private javax.swing.JComboBox<String> combo_chamada_equipamento;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_chamada;
    private javax.swing.JTable tbl_equip;
    private javax.swing.JFormattedTextField text_chamada_data;
    private javax.swing.JTextField text_chamada_desc;
    private javax.swing.JTextField text_chamada_titulo;
    private javax.swing.JFormattedTextField text_equip_data;
    private javax.swing.JTextField text_equip_fabricante;
    private javax.swing.JTextField text_equip_nome;
    private javax.swing.JTextField text_equip_preco;
    private javax.swing.JTextField text_equip_serie;
    // End of variables declaration//GEN-END:variables
}
