package com.DiagnozeIrGydimas;

import javafx.scene.control.Alert;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;


public class MainForm
{

	private JPanel MainForm;
	private JPanel Top;
	private JPanel Down;
	private JLabel DiagnozėPavadinimas;
	private  JButton RodykRod;
	private JButton RodPacient;
	private JButton LigSar;
	private JButton Duom;
	private JButton DarTyr;
	private JButton rodytiLigaBeiRekomendacijaButton;
	private static int IDD ;
	private String BlogasRod = null;
	private String BlogRodPav = null;

	private static String CONSTRING = "jdbc:sqlserver://DESKTOP-A0C0U6M;database=LigosDiagnoze";
	private  static String usrn= "sa";
	private  static String pass = "vasara";


	public static void infoBox(String infoMessage, String titleBar, String headerMessage)
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}



	private static DefaultTableModel buildTableModel(ResultSet rs)
			throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}





	private MainForm()
	{
		RodykRod.addActionListener(e ->
		{
			//JOptionPane.showMessageDialog(null, Rodikliai.values());

			JOptionPane.showMessageDialog(null,SQL.Select("Pavadinimas","rodiklis"));
		});
		RodPacient.addActionListener(e -> JOptionPane.showMessageDialog(null, SQL.Select("PacientoID, Vardas, Pavarde", "Pacientas")));
		LigSar.addActionListener(e -> JOptionPane.showMessageDialog(null, "Ligų sąrašas: \n Trombocitopenija\n " +
				"Neutrofilų trūkumas \n Monocitų trūkumas \n Limfocitų trūkumas \n" +
				"Eritrocitų trūkumas "));
		Duom.addActionListener(e ->
		{
			//rodyt pacientą

			try{
				Connection con = DriverManager.getConnection(CONSTRING,usrn,pass);
				Statement st = con.createStatement();

				ResultSet pac;
				pac = st.executeQuery("SELECT Pacientas.Vardas, Pavarde FROM Pacientas WHERE PacientoID = " + IDD);

				JTable table = new JTable(buildTableModel(pac));

				JOptionPane.showMessageDialog(null, new JScrollPane(table));


			}catch (Exception exc)
			{
				exc.printStackTrace();
			}

		});
		// daryti tyrimą
		DarTyr.addActionListener(e ->
		{

			try{
				Connection con = DriverManager.getConnection(CONSTRING,usrn,pass);
				Statement st = con.createStatement();

				ResultSet tyr;
				tyr = st.executeQuery("SELECT Pacientas.Vardas, Pacientas.Pavarde, Rodiklis.Pavadinimas ,RodiklioRodmenys, Rodiklis.Rmin, Rmax" +
						" FROM Tyrimas1 " +
						"INNER JOIN Pacientas ON " +
						"Pacientas.PacientoID = Tyrimas1.PacientoID" +
						" INNER JOIN Rodiklis ON" +
						" Tyrimas1.RodiklioID = Rodiklis.RodiklioID WHERE Pacientas.PacientoID=" + IDD);

				JTable table1 = new JTable(buildTableModel(tyr));

				JOptionPane.showMessageDialog(null, new JScrollPane(table1));






			}catch (Exception exc)
			{
				exc.printStackTrace();
			}


		});
		// diagnozė
		rodytiLigaBeiRekomendacijaButton.addActionListener(e ->
		{

			try{
				Connection con = DriverManager.getConnection(CONSTRING,usrn,pass);
				Statement st = con.createStatement();

				ResultSet diagn;


				diagn = st.executeQuery("SELECT Pacientas.Vardas, Pacientas.Pavarde, Rodiklis.Pavadinimas, Tyrimas1.RodiklioRodmenys, Rodiklis.Rmin, Rodiklis.Rmax  " +
						"FROM Tyrimas1" +
						" INNER JOIN Pacientas ON Pacientas.PacientoID = Tyrimas1.PacientoID " +
						"INNER JOIN Rodiklis ON  Tyrimas1.RodiklioID = Rodiklis.RodiklioID " +
						"WHERE Pacientas.PacientoID= "+IDD+" AND RodiklioRodmenys < Rodiklis.Rmin OR Tyrimas1.RodiklioRodmenys > Rodiklis.Rmax" );

				System.out.println(IDD);

				if (diagn.next())
				{
					BlogasRod = diagn.getString("RodiklioRodmenys");
					BlogRodPav = diagn.getString("Pavadinimas");
				}

				System.out.println(BlogasRod);
				System.out.println(BlogRodPav);

				if(BlogRodPav.equals("Trombocitai"))
				{
					JOptionPane.showMessageDialog(null,
							"Trombocitopenija- Trombocitopenija gali būti gydoma medikamentais, chirurginiais gydymo metodais ir perpilant kraują.\n Reikia kreiptis į gydytoją dėl tolimesnės gydimo eigos",
							"Gydimas",
							JOptionPane.WARNING_MESSAGE);
				}
				else if (BlogRodPav.equals("Neutrofilai"))
				{
					{
						JOptionPane.showMessageDialog(null,
								"Neutrofilai byloja apie bakterinį uždegimą Reikia kreiptis į gydytoją dėl tolimesnės gydimo eigos",
								"Gydimas",
								JOptionPane.WARNING_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Pacientas sveikas!");
				}


			}catch (Exception exc)
			{
				exc.printStackTrace();
			}



		});
	}


	public static void main(String[] args)
	{

		IDD = Integer.parseInt(JOptionPane.showInputDialog("Iveskite paciento ID"));

		JFrame frame = new JFrame("Diagnozė ir gydymas");

		JPanel Center =new JPanel();
		Center.setBackground(Color.BLACK);
		frame.getContentPane();

		JTextField textField1 = new JTextField();
		textField1.setPreferredSize(new Dimension(100,15));
		Center.add(textField1);



		frame.setPreferredSize(new Dimension(300,250));
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new MainForm().MainForm);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);



	}




}
