package calculadoragui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tarea 4 entornos Clase para diseñar una calculadora
 *
 * @author jose
 * @version 1.1
 */
public class Interfaz implements ActionListener {

    JTextField textPantUp, textPantDown;
    Panel panelN, panelB1, panelB3;
    JPanel panelS, panelB2;
    JButton buttonMC, buttonMR, buttonMS, buttonMemMas, buttonMemMenos, buttonsNumeros[], buttonOperaciones[];
    String textPanOper[] = {"R", "C", "+", "/", "-", "*", "="}, ax = "";
    float num1, num2, nr, M;//variables para las buttonOperaciones
    int tipOp; //para controlar el tipo de operacion que se realiza
    boolean newNum = false;//control sobre escribir un nuevo numero despues de alguna operacion cambia a true cuando se ha realizado una operacion

    /**
     * Posicionamiento inicial de los elementos
     */
    public Interfaz() {

        JFrame jfMain = new JFrame("Calculator");
        jfMain.setLayout(new BorderLayout(4, 4));

        norte();
        sur();

        jfMain.add(panelN, BorderLayout.NORTH);
        jfMain.add(panelS, BorderLayout.CENTER);

        jfMain.setLocation(100, 80);
        jfMain.setResizable(false);
        jfMain.setVisible(true);
        jfMain.setSize(300, 380);
        jfMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Posicionamiento de los numeros/signos de la parte superior de la pantalla
     * de resultados de la calculadora
     *
     *
     */
    public void norte() {

        panelN = new Panel(null);

        textPantUp = new JTextField("");
        textPantDown = new JTextField("0");

        textPantUp.setHorizontalAlignment(JTextField.RIGHT);
        textPantDown.setHorizontalAlignment(JTextField.RIGHT);

        //Quitar bordes a los campos de texto
        textPantUp.setBorder(BorderFactory.createLineBorder(Color.white));
        textPantDown.setBorder(BorderFactory.createLineBorder(Color.white));

        //desabilitando los campos de texto
        textPantUp.setEditable(false);
        textPantDown.setEditable(false);

        textPantUp.setBackground(Color.white);
        textPantDown.setBackground(Color.white);

        panelN.add(textPantUp);
        panelN.add(textPantDown);

        textPantUp.setBounds(35, 10, 200, 15);
        textPantDown.setBounds(35, 25, 200, 30);

        panelN.setSize(270, 47);
        panelN.setVisible(true);

    }

    /**
     *
     * Posicionamiento de los numeros/signos de la parte inferior de la pantalla
     * de resultados de la calculadora
     */
    public void sur() {

        panelS = new JPanel(new BorderLayout(6, 50));
        panelS.setLayout(new BorderLayout(4, 4));

        botMem();
        botNum();
        botOpe();

        panelS.add(panelB1, BorderLayout.NORTH);
        panelS.add(panelB2, BorderLayout.CENTER);
        panelS.add(panelB3, BorderLayout.EAST);

        panelS.setSize(270, 330);
    }

    /**
     * Posicion en pantalla de los botones de memoria
     *
     *
     */
    public void botMem() {

        panelB1 = new Panel(null);

        buttonMC = new JButton("MC");
        buttonMR = new JButton("MR");
        buttonMS = new JButton("MS");
        buttonMemMas = new JButton("M+");
        buttonMemMenos = new JButton("M-");

        buttonMC.setFont(new Font("Arial", Font.BOLD, 11));
        buttonMR.setFont(new Font("Arial", Font.BOLD, 11));
        buttonMS.setFont(new Font("Arial", Font.BOLD, 11));
        buttonMemMas.setFont(new Font("Arial", Font.BOLD, 11));
        buttonMemMenos.setFont(new Font("Arial", Font.BOLD, 11));

        buttonMC.setMargin(new Insets(1, 1, 1, 1));
        buttonMR.setMargin(new Insets(1, 1, 1, 1));
        buttonMS.setMargin(new Insets(1, 1, 1, 1));
        buttonMemMas.setMargin(new Insets(1, 1, 1, 1));
        buttonMemMenos.setMargin(new Insets(1, 1, 1, 1));

        buttonMC.setBounds(35, 0, 33, 33);
        buttonMR.setBounds(78, 0, 33, 33);
        buttonMS.setBounds(121, 0, 33, 33);
        buttonMemMas.setBounds(164, 0, 33, 33);
        buttonMemMenos.setBounds(207, 0, 33, 33);

        panelB1.add(buttonMC);
        panelB1.add(buttonMR);
        panelB1.add(buttonMS);
        panelB1.add(buttonMemMas);
        panelB1.add(buttonMemMenos);

        buttonMC.addActionListener(this);
        buttonMR.addActionListener(this);
        buttonMS.addActionListener(this);
        buttonMemMas.addActionListener(this);
        buttonMemMenos.addActionListener(this);

        panelB1.setSize(270, 45);
        panelB1.setVisible(true);
    }

    /**
     *
     * Posicion en pantalla de los botones de los numeros
     *
     */
    public void botNum() {

        panelB2 = new JPanel(null);

        int nx3 = 121, nx2 = 121, nx1 = 121, n3y = 0, n2y = 43, n1y = 86;
        buttonsNumeros = new JButton[11];

        /**
         *  *****************************************
         * bloque para crear los botones, añadirlos y asignar buttonsNumeros
         *
         */
        for (int i = 0; i <= 10; i++) {

            if (i <= 9) {
                buttonsNumeros[i] = new JButton("" + i);
                panelB2.add(buttonsNumeros[i]);
                buttonsNumeros[i].setMargin(new Insets(1, 1, 1, 1));
                buttonsNumeros[i].addActionListener(this);
            } else {
                buttonsNumeros[i] = new JButton(".");
                panelB2.add(buttonsNumeros[i]);
                buttonsNumeros[i].setMargin(new Insets(1, 1, 1, 1));
                buttonsNumeros[i].addActionListener(this);
            }
        }
        /**
         *  ******************************************
         * bloque para posicionar botones
         */

        for (int i = 10; i >= 0; i--) {

            if (i == 10) {
                buttonsNumeros[i].setBounds(121, 129, 35, 35);
            } else {
                if (i <= 9 && i >= 7) {
                    buttonsNumeros[i].setBounds(nx3, n3y, 35, 35);
                    nx3 -= 43;
                } else if (i <= 6 && i >= 4) {
                    n3y += 43;
                    buttonsNumeros[i].setBounds(nx2, n2y, 35, 35);
                    nx2 -= 43;
                } else if (i <= 3 && i >= 1) {
                    n3y += 43;
                    buttonsNumeros[i].setBounds(nx1, n1y, 35, 35);
                    nx1 -= 43;
                } else if (i == 0) {
                    buttonsNumeros[i].setBounds(35, 129, 78, 35);
                }
            }
        }

        panelB2.setSize(170, 150);
        panelB2.setVisible(true);
    }

    /**
     * Posicion en pantalla de los botones de las operaciones
     */
    public void botOpe() {

        panelB3 = new Panel(null);

        int c = 0, x = 0, y = 0;

        buttonOperaciones = new JButton[7];

        for (int i = 0; i <= 6; i++) {
            if (c <= 1) {

                buttonOperaciones[i] = new JButton(textPanOper[i]);
                panelB3.add(buttonOperaciones[i]);

                buttonOperaciones[i].setBounds(x, y, 30, 35);

                buttonOperaciones[i].setMargin(new Insets(1, 1, 1, 1));
                buttonOperaciones[i].addActionListener(this);
                x += 33;
                c++;
            } else {
                if (i == 6) {
                    x = 0;
                    y += 43;
                    buttonOperaciones[i] = new JButton(textPanOper[i]);
                    panelB3.add(buttonOperaciones[i]);

                    buttonOperaciones[i].setBounds(x, y, 65, 35);

                    buttonOperaciones[i].setMargin(new Insets(1, 1, 1, 1));
                    buttonOperaciones[i].addActionListener(this);
                    x += 33;
                    c++;
                } else {
                    c = 0;
                    x = 0;
                    y += 43;
                    buttonOperaciones[i] = new JButton(textPanOper[i]);
                    panelB3.add(buttonOperaciones[i]);

                    buttonOperaciones[i].setBounds(x, y, 30, 35);

                    buttonOperaciones[i].setMargin(new Insets(1, 1, 1, 1));
                    buttonOperaciones[i].addActionListener(this);
                    x += 33;
                    c++;
                }
            }

        }

        panelB3.setVisible(true);
        panelB3.setSize(120, 200);
    }

    /**
     * @param ax; se le pasa el string para comprobar si existe
     * @return booleano
     */
    public boolean noExiste(String ax) {

        try {
            int n = Integer.parseInt(ax);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * Llamada a los metodos que tienen funcionalidades determinadas
     *
     * @param e; le pasamos un evento
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String op = "";
        if (e.getActionCommand().equals("+")) {//boton suma
            botonSumar();
        }
        if (e.getActionCommand().equals("-")) {//cuando se decide restar
            botonRestar();
        }
        if (e.getActionCommand().equals("*")) {//cuando se decide multiplicar
            botonMultiplicar();
        }
        if (e.getActionCommand().equals("/")) {//cuando se decide dividir

            botonDividir();

        }
        if (e.getActionCommand().equals("=") && !textPantDown.getText().equals("")) {

            funcionalidadOperaciones();

        }
        if (noExiste(e.getActionCommand())) { //cuando se oprimen buttonsNumeros

            pulsarNumeros(e);

        } else {//cuando se oprime el resto de botones
            restoBotones(e);

        }
    }

    /**
     * Acción al pulsar los numeros del 0 al 9
     *
     * @param e; Le pasamos el envento al pulsar numeros
     */
    public void pulsarNumeros(ActionEvent e) {

        if (textPantUp.getText().equals("")) {
            ax += e.getActionCommand();
            textPantDown.setText(ax);
        } else {
            if (tipOp == 0) {
                if (newNum) {
                    ax = "";

                    textPantUp.setText(textPantDown.getText());
                    ax += e.getActionCommand();
                    textPantDown.setText(ax);
                    newNum = false;
                } else {
                    ax = "";
                    ax += textPantDown.getText() + e.getActionCommand();
                    textPantDown.setText(ax);
                }
            } else {
                ax = "";
                ax += textPantDown.getText() + e.getActionCommand();
                textPantDown.setText(ax);
            }
        }

    }

    /**
     * Accion al pulsar cualquier boton que no sea numérico y de operaciones
     *
     * @param e; Le pasamos el evento del resto de los botones
     */
    public void restoBotones(ActionEvent e) {
        if (e.getActionCommand().equals("R")) {
            textPantUp.setText("");
            Float a = Float.parseFloat(textPantDown.getText());
            textPantDown.setText("" + Math.sqrt(a));
        }
        if (e.getActionCommand().equals("C")) { //para reiniciar valores y limpiar pantalla
            tipOp = 0;
            num1 = 0;
            num2 = 0;
            nr = 0;
            textPantUp.setText("");
            textPantDown.setText("0");
            ax = "";
        }
        if (e.getActionCommand().equals("MC")) {//para limpiar la memoria de la calculadora
            buttonMS.setForeground(Color.black);
            textPantUp.setText("");
            textPantDown.setText("0");
            M = 0;
        }
        if (e.getActionCommand().equals("MR")) {//para mostrar valor almacenado en la memoria
            textPantUp.setText("");
            textPantDown.setText(String.valueOf(M));
        }
        if (e.getActionCommand().equals("MS")) {//guardar un valor en la memoria
            buttonMS.setForeground(Color.red);
            M = Float.parseFloat(textPantDown.getText());
        }
        if (e.getActionCommand().equals("M+")) {//sumar valor de la pantalla con el valor de la memoria
            M += Float.parseFloat(textPantDown.getText());
        }
        if (e.getActionCommand().equals("M-")) {//restar valor de la pantalla con el valor de la memoria
            M -= Float.parseFloat(textPantDown.getText());
        }
        if (e.getActionCommand().equals(".")) {//usar el punto para los decimales
            ax = "";
            if (buttonsNumeros[10].isEnabled()) {
                buttonsNumeros[10].setEnabled(false);
                ax = textPantDown.getText() + ".";
                textPantDown.setText(ax);
            }
        }
    }

    /**
     * Funcionalidad al pulsar el boton de la suma
     */
    public void botonSumar() {
        buttonsNumeros[10].setEnabled(true);
        ax = "";
        if (tipOp == 1) {

        } else if (tipOp == 0) {//validacion para no chocar con otras buttonOperaciones
            if (textPantUp.getText().equals("")) {
                num1 = Float.parseFloat(textPantDown.getText());
                ax += textPantUp.getText() + textPantDown.getText();
                textPantUp.setText(ax + " + ");
                textPantDown.setText("");
                tipOp = 1;
            } else {
                if (!newNum) {//validacion para nueva operacion
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantDown.getText();
                    textPantUp.setText(ax + " + ");
                    textPantDown.setText("");
                    tipOp = 1;
                } else {//usar otras buttonOperaciones con la suma
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantUp.getText();
                    textPantUp.setText(ax + " + ");
                    textPantDown.setText("");
                    tipOp = 1;
                }
            }
        }

    }

    /**
     * Funcionalidad al pulsar el botón de la resta
     */
    public void botonRestar() {

        buttonsNumeros[10].setEnabled(true);
        ax = "";
        if (tipOp == 2) {

        } else if (tipOp == 0) {//validacion para no chocar con otras buttonOperaciones
            if (textPantUp.getText().equals("")) {
                num1 = Float.parseFloat(textPantDown.getText());
                ax += textPantUp.getText() + textPantDown.getText();
                textPantUp.setText(ax + " - ");
                textPantDown.setText("");
                tipOp = 2;
            } else {
                if (!newNum) {//validacion para nueva operacion
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantDown.getText();
                    textPantUp.setText(ax + " - ");
                    textPantDown.setText("");
                    tipOp = 2;
                } else {//usar otras buttonOperaciones con la suma
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantUp.getText();
                    textPantUp.setText(ax + " - ");
                    textPantDown.setText("");
                    tipOp = 2;
                }
            }
        }
    }

    /**
     * Funcionalidad al pulsar el botón de la multiplicación
     */
    public void botonMultiplicar() {
        buttonsNumeros[10].setEnabled(true);
        ax = "";
        if (tipOp == 3) {

        } else if (tipOp == 0) {//validacion para no chocar con otras buttonOperaciones
            if (textPantUp.getText().equals("")) {
                num1 = Float.parseFloat(textPantDown.getText());
                ax += textPantUp.getText() + textPantDown.getText();
                textPantUp.setText(ax + " * ");
                textPantDown.setText("");
                tipOp = 3;
            } else {
                if (!newNum) {//validacion para nueva operacion
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantDown.getText();
                    textPantUp.setText(ax + " * ");
                    textPantDown.setText("");
                    tipOp = 3;
                } else {//usar otras buttonOperaciones con la suma
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantUp.getText();
                    textPantUp.setText(ax + " * ");
                    textPantDown.setText("");
                    tipOp = 3;
                }
            }
        }
    }

    /**
     * Funcionalidad al pulsar el botón de la división
     */
    public void botonDividir() {
        buttonsNumeros[10].setEnabled(true);
        ax = "";
        if (tipOp == 4) {

        } else if (tipOp == 0) {//validacion para no chocar con otras buttonOperaciones
            if (textPantUp.getText().equals("")) {
                num1 = Float.parseFloat(textPantDown.getText());
                ax += textPantUp.getText() + textPantDown.getText();
                textPantUp.setText(ax + " / ");
                textPantDown.setText("");
                tipOp = 4;
            } else {
                if (!newNum) {//validacion para nueva operacion
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantDown.getText();
                    textPantUp.setText(ax + " / ");
                    textPantDown.setText("");
                    tipOp = 4;
                } else {//usar otras buttonOperaciones con la suma
                    num1 = Float.parseFloat(textPantDown.getText());
                    ax += textPantUp.getText();
                    textPantUp.setText(ax + " / ");
                    textPantDown.setText("");
                    tipOp = 4;
                }
            }
        }
    }

    /**
     * Asignar funcionalidad a las operaciones que se van llamando
     */
    public void funcionalidadOperaciones() {
        newNum = true;
        if (tipOp == 1) {//operacion para la suma
            tipOp = 0;
            ax = "";
            ax += textPantUp.getText() + textPantDown.getText();
            textPantUp.setText(ax);
            num2 = Float.parseFloat(textPantDown.getText());
            nr = num1 + num2;
            textPantDown.setText(String.valueOf(nr));
        } else if (tipOp == 2) { //operacion para la resta
            tipOp = 0;
            ax = "";
            ax += textPantUp.getText() + textPantDown.getText();
            textPantUp.setText(ax);
            num2 = Float.parseFloat(textPantDown.getText());
            nr = num1 - num2;
            textPantDown.setText(String.valueOf(nr));
        }
        if (tipOp == 3) { //operacion para la multiplicacion
            tipOp = 0;
            ax = "";
            ax += textPantUp.getText() + textPantDown.getText();
            textPantUp.setText(ax);
            num2 = Float.parseFloat(textPantDown.getText());
            nr = num1 * num2;
            textPantDown.setText(String.valueOf(nr));
        }
        if (tipOp == 4) { //operacion para la multiplicacion
            if (Float.parseFloat(textPantDown.getText()) != 0) {
                tipOp = 0;
                ax = "";
                ax += textPantUp.getText() + textPantDown.getText();
                textPantUp.setText(ax);
                num2 = Float.parseFloat(textPantDown.getText());
                nr = num1 / num2;
                textPantDown.setText(String.valueOf(nr));
            }

        }
    }

}
