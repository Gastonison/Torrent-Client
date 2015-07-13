//Michael Murray, Gaston Gonzalez, Felica Yau

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mike
 */
public class GUI extends javax.swing.JFrame {

	/**
	 * Creates new form NewJFrame
	 */


	private javax.swing.JTextField File_Name;
	private javax.swing.JTextField File_Size;
	private javax.swing.JTextField Pieces_Downloaded;
	private javax.swing.JTextField Pieces_Uploaded;
	private javax.swing.JTextField Total_Pieces;
	private javax.swing.JLabel catIcon;
	private javax.swing.JPanel cats;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JTextField jTextField4;
	private javax.swing.JTextField jTextField5;
	private javax.swing.JTextField jTextField6;
	ArrayList<Peer> plist;
	RandomAccessFile theFile;
	
	public GUI(final ArrayList<Peer> plist, RandomAccessFile thefile) {
		this.plist=plist;
		this.theFile=thefile;

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String[][] information = new String[50][50];
		jPanel2 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jPanel1 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jTextField3 = new javax.swing.JTextField();
		jTextField4 = new javax.swing.JTextField();
		jTextField5 = new javax.swing.JTextField();
		jTextField6 = new javax.swing.JTextField();
		File_Name = new javax.swing.JTextField();
		Pieces_Downloaded = new javax.swing.JTextField();
		Pieces_Uploaded = new javax.swing.JTextField();
		Total_Pieces = new javax.swing.JTextField();
		File_Size = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jProgressBar1 = new javax.swing.JProgressBar();
		cats = new javax.swing.JPanel();
		catIcon = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jTable1.setModel(new javax.swing.table.DefaultTableModel(
				information,
				new String [] {
						"Peer", "Choked", "Interested", "Downloaded", "Uploaded", "Port", "IP", "Name"
				}
				) {
			boolean[] canEdit = new boolean [] {
					false, false, false, false, false, false, false, false
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		});
		jScrollPane1.setViewportView(jTable1);
		if (jTable1.getColumnModel().getColumnCount() > 0) {
			jTable1.getColumnModel().getColumn(0).setResizable(false);
			jTable1.getColumnModel().getColumn(1).setResizable(false);
			jTable1.getColumnModel().getColumn(2).setResizable(false);
			jTable1.getColumnModel().getColumn(3).setResizable(false);
			jTable1.getColumnModel().getColumn(4).setResizable(false);
			jTable1.getColumnModel().getColumn(5).setResizable(false);
			jTable1.getColumnModel().getColumn(6).setResizable(false);
			jTable1.getColumnModel().getColumn(7).setResizable(false);
		}

		ImageIcon imgThisImg = new ImageIcon("bitman.png");


		catIcon.setIcon(imgThisImg);
		catIcon.setPreferredSize(new Dimension(100,100));

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

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 100, Short.MAX_VALUE)
				);
		jPanel4Layout.setVerticalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 100, Short.MAX_VALUE)
				);

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel2Layout.createSequentialGroup()
										.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1048, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
										.addGroup(jPanel2Layout.createSequentialGroup()
												.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		jTextField1.setText("File Info");

		jTextField2.setText("File Name");

		jTextField3.setText("Pieces Downloaded");

		jTextField4.setText("Pieces Uploaded");

		jTextField5.setText("Total Pieces");

		jTextField6.setText("File Size");

		jButton1.setText("Quit");
		
		jButton1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					close(plist,theFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(1);
				
			}
			
		});
		
	

		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Container frame = jButton1.getParent();
				do 
					frame = frame.getParent(); 
				while (!(frame instanceof JFrame));                                      
				((JFrame) frame).dispose();
			}
		});

		javax.swing.GroupLayout catsLayout = new javax.swing.GroupLayout(cats);
		cats.setLayout(catsLayout);
		catsLayout.setHorizontalGroup(
				catsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(catIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				);
		catsLayout.setVerticalGroup(
				catsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(catIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				);

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(jTextField1)
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(jTextField2)
												.addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
												.addComponent(jTextField4)
												.addComponent(jTextField5)
												.addComponent(jTextField6))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
														.addComponent(File_Name)
														.addComponent(Pieces_Downloaded)
														.addComponent(Pieces_Uploaded)
														.addComponent(Total_Pieces)
														.addComponent(File_Size, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																.addGroup(jPanel3Layout.createSequentialGroup()
																		.addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addComponent(cats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				);
		jPanel3Layout.setVerticalGroup(
				jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(File_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(Pieces_Downloaded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																.addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(Pieces_Uploaded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																.addGap(8, 8, 8)
																.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(Total_Pieces, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																		.addComponent(cats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																				.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(File_Size, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(jButton1))
																						.addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																						.addGap(0, 6, Short.MAX_VALUE))
				);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		pack();
	}






	public void addPeer(PeerInformation peer,int row){
		System.out.println(peer==null);
		System.out.println(peer.getName());
		for(int x = 0;x<8;x++){
			if(x==0){
				jTable1.getModel().setValueAt(row, row, x);
			}
			if(x==1){
				jTable1.getModel().setValueAt(peer.they_choking, row, x);
			}
			if(x==2){
				jTable1.getModel().setValueAt(peer.they_interested, row, x);
			}
			if(x==3){
				jTable1.getModel().setValueAt(peer.downloaded, row, x);
			}
			if(x==4){
				jTable1.getModel().setValueAt(peer.uploaded, row, x);
			}
			if(x==5){
				jTable1.getModel().setValueAt(peer.port, row, x);
			}
			if(x==6){
				jTable1.getModel().setValueAt(peer.ip, row, x);
			}
			if(x==7){
				jTable1.getModel().setValueAt(peer.name, row, x);
			}

		}



	}
	public void updateDownloaded(int row,int downloaded){
		jTable1.getModel().setValueAt(downloaded, row, 3);
	}
	public void updateUploaded(int row,int uploaded){
		jTable1.getModel().setValueAt(uploaded, row, 4);
	}
	public void updateInterested(int row,boolean interested){
		jTable1.getModel().setValueAt(interested, row, 2);
	}
	public void updateChoked(int row,boolean interested){
		jTable1.getModel().setValueAt(interested, row, 1);
	}
	
	public void increaseActionBar(int fileSize, int piecesDownloaded){
		double a = fileSize;
		double b = piecesDownloaded;

		double f = a/b;

		double l = f*100;


		jProgressBar1.setValue((int) l);
	}

	public void setInformation(){

	}
	
	public void setName(String name){
		File_Name.setText(name);
	}
	public void setDownload(int downloaded){

		Pieces_Downloaded.setText(downloaded+ "");
	}
	public void setUpload(int uploaded){
		
		Pieces_Uploaded.setText(uploaded+"");
	}
	public void totalPiece(int fileLength){
		
		Total_Pieces.setText(fileLength+"");
	}
	public void fileSize(int fileLength){
		
		File_Size.setText(fileLength+ "");
	}
	 public void close(ArrayList<Peer> plist, RandomAccessFile thefile) throws IOException {
		   for (int i = 0; i < plist.size(); i++) {
		     if (plist.get(i).input != null) plist.get(i).input.close();
		     if (plist.get(i).output != null) plist.get(i).output.close();

		   }
		   thefile.close();
		 }
	
                 
}
