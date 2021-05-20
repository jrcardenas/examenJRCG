package interfaz;

import accesodatos3.Category;
import accesodatos3.Film;
import accesodatos3.HibernateUtil;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Interfaz de usuario para la búsqueda en la base de datos.
 *
 * @author JAVIER 
 */
public class MainWindow extends javax.swing.JFrame {

    //Modelo de la tabla de resultados
    private static DefaultTableModel modeloTabla = null;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        iniciarTabla();

        //Como este metodo realiza una consulta, mejor que lo cargue al arrancar el programa 
        //y no cada vez que se abra el dialogo de seleccion de cateogoria
        cargarComboCategorias();

        //Carga del comboBox de caracteristicas especiales
        cargarComboEsp();
    }

    //***************METODO DE CONSULTAS HQL******************
    /**
     * Abre la sesion y realiza las consuiltas HQL sobre la base de datos.
     *
     * @param consulta Cadena con la consulta que se quiere lanzar.
     * @return List con los resultados.
     */
    public List lanzarConsulta(String consulta) {
        //Inicio la variable que recogera los resultados
        List resultList = null;
        //Inicio la sesion con HibernateUtil. No va en try-catch porque HibernateUtil ya lanza excepcion si hay fallo.
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        //Capturamos toda excepcion que pueda ocurrir al lanzar una consulta
        try {
            if (sesion != null) {
                Query query = sesion.createQuery(consulta);
                resultList = query.list();
                sesion.getTransaction();
            }
        } catch (Exception e) {
            String errQuery = "Ha ocurrido un error inesperado lanzando la consulta";
            System.err.println(errQuery);
            JOptionPane.showMessageDialog(this, errQuery, "ERROR", 0);
        } finally {
            //Cerramos la sesion
            sesion.close();
        }
        return resultList;
    }

    //************METODOS PROPIOS DE MANEJO DE LA INTERFAZ***********
    /**
     * Abre el dialogo de busqueda de peliculas por nombre.
     */
    public void abrirBusquedaPorNombre() {
        busquedaNombre.pack();
        busquedaNombre.setLocationRelativeTo(this);
        busquedaNombre.setVisible(true);
    }

    /**
     * Abre el dialogo de busqueda por categoria.
     *
     */
    public void abrirBusquedaPorCat() {
        busquedaCat.pack();
        busquedaCat.setLocationRelativeTo(this);
        busquedaCat.setVisible(true);
    }

    /**
     * Abre el dialogo de busqueda por caracteristicas especiales.
     *
     */
    public void abrirBusquedaPorEsp() {
        busquedaEsp.pack();
        busquedaEsp.setLocationRelativeTo(this);
        busquedaEsp.setVisible(true);
    }

    /**
     * Sale del programa.
     */
    public void salir() {
        this.dispose();
        System.exit(0);
        System.out.println("Programa finalizado");
    }

    /**
     * Inicia y configura la tabla de resultados:
     * <b>1:</b> Establece el tipo de dato para cada columna.
     * <b>2:</b> Carga las cabeceras.
     * <b>3:</b> Establece el ancho de las columnas.
     */
    public void iniciarTabla() {
        //Reajustamos el modelo la tabla para que definir que tipo de dato da cada columna
        //Con esto conseguimos que funcione correctamente la reordenación de filas por columna
        modeloTabla = new DefaultTableModel() {
            Class[] tiposDeColumna = new Class[]{
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            @Override
            public Class getColumnClass(int indiceColumna) {
                return tiposDeColumna[indiceColumna];
            }
        };
        //Damos la posibilidad a la tabla de reordenarse en funcion de la columna
        //cuando el usuario pulsa sobre la cabecera
        jTabla.setAutoCreateRowSorter(true);

        //Cabeceras de las columnas, mostraremos solamente el titulo, duracion, calificacion y extras.
        modeloTabla.addColumn("Titulo");
        modeloTabla.addColumn("Duración (min.)");
        modeloTabla.addColumn("Calificación");
        modeloTabla.addColumn("Carac.Especiales");
        jTabla.setModel(modeloTabla);

        //Le damos a las columnas 'titulo' y 'caracteristicas especiales', un ancho mayor
        jTabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        jTabla.getColumnModel().getColumn(0).setPreferredWidth(150);
        //jTabla.getColumnModel().getColumn(1).setPreferredWidth(10); PRUEBA
        //jTabla.getColumnModel().getColumn(2).setPreferredWidth(10); PRUEBA

    }

    /**
     * Borra las filas de la tabla de resultados.
     *
     */
    public void borrarFilas() {
        int filas = modeloTabla.getRowCount();
        for (int i = 0; i < filas; i++) {
            modeloTabla.removeRow(0);
        }
        jTabla.setModel(modeloTabla);
    }

    /**
     * Carga el comboBox de caracteristicas especiales en el dialogo de
     * busqueda.
     *
     */
    public void cargarComboEsp() {
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        modeloCombo.addElement("Trailers");
        modeloCombo.addElement("Commentaries");
        modeloCombo.addElement("Deleted Scenes");
        modeloCombo.addElement("Behind the Scenes");
        comboEsp.setModel(modeloCombo);
    }

    /**
     * Carga el comboBox de categorias en el dialogo de busqueda. Realiza la
     * busqueda de categorias en la tabla <b>category</b> de la base de datos y
     * muestra cada resultado como una opcion del comboBox.
     */
    public void cargarComboCategorias() {
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        List listaCategorias = lanzarConsulta("FROM Category cat");
        //Comprobamos que no tengamos un objeto vacio
        if (!listaCategorias.isEmpty()) {
            //Recorremos las categorias recuperados y las vamos añadiendo al modelo
            for (Object categoria : listaCategorias) {
                Category cat = (Category) categoria;
                modeloCombo.addElement(cat.getName());
            }
            comboCat.setModel(modeloCombo);
        } else {
            //Si no se han recuperado las categorias, error.
            JOptionPane.showMessageDialog(this, "Error al cargar las categorías", "Error comboCategorias", 0);
        }
    }

    /**
     * Busca la descripcion de la pelicula seleccionada en la tabla. Recoge el
     * titulo seleccionado en la tabla y ordena la busqueda en la base de datos
     *
     * @see <b>buscarDescripcion()</b>;
     */
    public void verDescripcion() {
        int indice = jTabla.getSelectedRow();
        if (indice >= 0) {
            String titulo = jTabla.getValueAt(indice, 0).toString();
            //Ahora invocamos al metodo buscarDescripcion que buscara la pelicula seleccionada en la base de datos.
            buscarDescripcion(titulo);
        } else {
            JOptionPane.showMessageDialog(this, "No se ha seleccionado película.\nBusque una película, haga click sobre ella y pulse de nuevo el botón.", "Error", 0);
        }
    }

    /**
     * Carga un List con resultados en la tabla de resultados.
     *
     * @param resultados List con los resultados a cargar.
     */
    public void mostrarResultados(List resultados) {
        //Primero reiniciamos la tabla, la vaciamos.
        borrarFilas();
        //Recorremos el List si no viene vacio
        if (!resultados.isEmpty()) {
            for (Object resultado : resultados) {
                //Siempre son objetos de tipo Film, asi que parseamos.
                Film pelicula = (Film) resultado;
                //Recuperamos los campos que queremos que se muestren en la tabla.
                String titulo = pelicula.getTitle();
                int duracion = pelicula.getLength();
                String calificacion = pelicula.getRating();
                String esp = pelicula.getSpecialFeatures();
                //Cargamos el modelo de la tabla.
                modeloTabla.addRow(new Object[]{
                    titulo, duracion, calificacion, esp
                });
                jTabla.setModel(modeloTabla);
                etiWarning.setText(jTabla.getRowCount() + " títulos encontrados:"); //Asignamos el modelo a la tabla
            }
        } else {
            //Si viene vacio, mostrar mensaje (encima de la tabla).
            etiWarning.setText("No se ha encontrado nada, pruebe otro nombre o caracteres.");
        }

    }

    //***********FIN METODOS PROPIOS DE LA INTERFAZ************
    //BUSQUEDA POR TITULO. APARTADO 1 DEL ULTIMO PUNTO DE LA TAREA ONLINE.
    /**
     * Carga una consulta HQL sobre peliculas en funcion del titulo.
     *
     */
    public void buscarPorTitulo() {
        etiCargandoTitulo.setText("Intentando buscar. Espere.. (puede tardar unos segundos)");
        //Recogemos lo que haya introducido el usuario en el campo de texto.
        String tituloBuscar = txtNombrePeli.getText();
        //Comprobamos que no venga el campo vacio.
        if (!tituloBuscar.isEmpty()) {
            //Elaboramos la consulta
            String consulta = "FROM Film peli WHERE peli.title like '%";
            consulta += tituloBuscar + "%'";
            //Lanzamos la consulta. El metodo lanzarConsulta nos devolvera un List con todos los resultados.
            List resultado = lanzarConsulta(consulta);
            mostrarResultados(resultado);
            busquedaNombre.dispose();
        } else { //Si viene vacio, avisar al usuario.
            JOptionPane.showMessageDialog(this, "Introduzca al menos un caracter", "Error", 0);
        }
        etiCargandoTitulo.setText("");
    }

//BUSQUEDA POR CATEGORIA. APARTADO 2 DEL ULTIMO PUNTO DE LA TAREA ONLINE.
    /**
     * Carga una consulta HQL sobre peliculas con una determinada categoria.
     *
     */
    public void buscarPorCategoria() {
        //Recogemos lo seleccionado en el comboBox. Si el indice es mayor que 0, hay algo seleccionado
        //si no, es que viene en blanco.
        if (comboCat.getSelectedIndex() >= 0) {
            //Preparamos la consulta HQL
            String catBuscar = comboCat.getSelectedItem().toString();
            String consulta = "SELECT film.film FROM FilmCategory as film WHERE film.category.name LIKE '";
            consulta += catBuscar + "'";
            //Lanzamos la consulta. El metodo lanzarConsulta nos devolvera un List con todos los resultados.
            List resultado = lanzarConsulta(consulta);
            mostrarResultados(resultado);
            busquedaCat.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione al menos una categoría", "Error", 0);
        }
    }

    //BUSQUEDA POR CARACTERISTICAS ESPECIALES. ULTIMO APARTADO DEL ULTIMO PUNTO TAREA ONLINE.
    /**
     * Carga una consulta HQL sobre peliculas en funcion de su propiedad
     * 'caracteristicas especiales'
     *
     */
    public void buscarPorEspeciales() {
        //Recogemos el valor del comboBox
        if (comboEsp.getSelectedIndex() >= 0) {
            String espBuscar = comboEsp.getSelectedItem().toString();
            String consulta = "SELECT film FROM Film as film WHERE film.specialFeatures LIKE'%";
            consulta += espBuscar + "%'";
            List resultado = lanzarConsulta(consulta);
                //Mostramos resultados en tabla y cerramos dialogo.
                mostrarResultados(resultado);
                busquedaEsp.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione al menos una característica", "Error", 0);
        }
    }

    //OPCIONALES
    /**
     * Recupera todas las peliculas de la base de datos para mostrarlas en la
     * tabla.
     *
     */
    public void buscarTodas() {
        String consulta = "FROM Film as film";
        List resultado = lanzarConsulta(consulta);
        mostrarResultados(resultado);
    }

    /**
     * Busca la descripcion de una pelicula en la base de datos y la muestra en
     * un dialogo de informacion.
     *
     * @param titulo Titulo de la pelicula a buscar (String).
     */
    public void buscarDescripcion(String titulo) {
        //Comprobamos que no llega una cadena vacia.
        if (!titulo.isEmpty()) {
            //Preparamos la consulta HQL para buscar el objeto Film con dicho titulo.
            String consulta = "SELECT peli.description FROM Film peli WHERE peli.title like '";
            consulta += titulo + "'";
            List resultado = lanzarConsulta(consulta);
            //El metodo lanzarConsulta devuelve un LIST, pero como buscamos la descripcion de una pelicula
            //unica, dicho List vendra solo cona una fila, asi que solo necesitamos acceder al primer indice,
            //que ademas sera una cadena de texto.

            //Mostramos en un dialogo
            JOptionPane.showMessageDialog(this, resultado.get(0), "Descripción de la película", 1);
        } else {
            etiWarning.setText("No ha seleccionado nada");
        }
    }

    //----------------INICIO CODIGO AUTOGENERADO--------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        busquedaNombre = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombrePeli = new javax.swing.JTextField();
        btnBuscarTitulo = new javax.swing.JButton();
        etiCargandoTitulo = new javax.swing.JLabel();
        btnSalirNom = new javax.swing.JButton();
        busquedaCat = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        comboCat = new javax.swing.JComboBox<>();
        btnBuscarCat = new javax.swing.JButton();
        btnSalirCat = new javax.swing.JButton();
        busquedaEsp = new javax.swing.JDialog();
        comboEsp = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        btnBuscarEsp = new javax.swing.JButton();
        btnSalirEsp = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnNombre = new javax.swing.JButton();
        btnCat = new javax.swing.JButton();
        btnCatEsp = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabla = new javax.swing.JTable();
        etiWarning = new javax.swing.JLabel();
        btnDescrip = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        etiLogo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnTodas = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        busquedaNombre.setTitle("TITULO");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Introduca el título de la película a buscar:");

        jLabel2.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        jLabel2.setText("(Puede introducir unos caracteres para una búsqueda más general)");

        btnBuscarTitulo.setText("Buscar");
        btnBuscarTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTituloActionPerformed(evt);
            }
        });

        btnSalirNom.setText("Salir");
        btnSalirNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirNomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout busquedaNombreLayout = new javax.swing.GroupLayout(busquedaNombre.getContentPane());
        busquedaNombre.getContentPane().setLayout(busquedaNombreLayout);
        busquedaNombreLayout.setHorizontalGroup(
            busquedaNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaNombreLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(busquedaNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombrePeli)
                    .addGroup(busquedaNombreLayout.createSequentialGroup()
                        .addComponent(btnBuscarTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalirNom, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(etiCargandoTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        busquedaNombreLayout.setVerticalGroup(
            busquedaNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaNombreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNombrePeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(etiCargandoTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(busquedaNombreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarTitulo)
                    .addComponent(btnSalirNom))
                .addContainerGap())
        );

        busquedaCat.setTitle("CATEGORIAS");

        jLabel3.setText("Seleccione categoría:");

        comboCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscarCat.setText("Buscar");
        btnBuscarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCatActionPerformed(evt);
            }
        });

        btnSalirCat.setText("Salir");
        btnSalirCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirCatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout busquedaCatLayout = new javax.swing.GroupLayout(busquedaCat.getContentPane());
        busquedaCat.getContentPane().setLayout(busquedaCatLayout);
        busquedaCatLayout.setHorizontalGroup(
            busquedaCatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaCatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(busquedaCatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(busquedaCatLayout.createSequentialGroup()
                        .addGroup(busquedaCatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboCat, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, busquedaCatLayout.createSequentialGroup()
                                .addComponent(btnBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSalirCat, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        busquedaCatLayout.setVerticalGroup(
            busquedaCatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaCatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(busquedaCatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCat)
                    .addComponent(btnSalirCat))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        busquedaEsp.setTitle("CARACTERISTICAS");

        comboEsp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Seleccione la característica especial:");

        btnBuscarEsp.setText("Buscar");
        btnBuscarEsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarEspActionPerformed(evt);
            }
        });

        btnSalirEsp.setText("Salir");
        btnSalirEsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirEspActionPerformed(evt);
            }
        });

        jLabel6.setText("Trailers = Trailer/Avances");

        jLabel7.setText("Commentaries = Comentarios del Director");

        jLabel8.setText("Behind The Scenes = Fuera de cámara");

        jLabel9.setText("Deleted Scenes = Escenas eliminadas");

        javax.swing.GroupLayout busquedaEspLayout = new javax.swing.GroupLayout(busquedaEsp.getContentPane());
        busquedaEsp.getContentPane().setLayout(busquedaEspLayout);
        busquedaEspLayout.setHorizontalGroup(
            busquedaEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaEspLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(busquedaEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(busquedaEspLayout.createSequentialGroup()
                        .addComponent(btnBuscarEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalirEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(busquedaEspLayout.createSequentialGroup()
                        .addGroup(busquedaEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboEsp, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        busquedaEspLayout.setVerticalGroup(
            busquedaEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(busquedaEspLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboEsp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(busquedaEspLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarEsp)
                    .addComponent(btnSalirEsp))
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ACCESO A DATOS - HIBERNATE - Menú Principal");
        setResizable(false);

        btnNombre.setText("Por Título");
        btnNombre.setToolTipText("Pulse para buscar películas por su nombre");
        btnNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNombreActionPerformed(evt);
            }
        });

        btnCat.setText("Por Categorías");
        btnCat.setToolTipText("Pulse para buscar películas por categoría");
        btnCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatActionPerformed(evt);
            }
        });

        btnCatEsp.setText("Por Especiales");
        btnCatEsp.setToolTipText("Pulse para buscar películas por especiales");
        btnCatEsp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatEspActionPerformed(evt);
            }
        });

        jTabla.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTabla.setToolTipText("Tabla de resultados");
        jTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(jTabla);

        etiWarning.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        btnDescrip.setText("Ver Descripción");
        btnDescrip.setToolTipText("Ver descripción de la película seleccionada");
        btnDescrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescripActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir del programa");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        etiLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cine.png"))); // NOI18N
        etiLogo.setText("jLabel5");
        etiLogo.setToolTipText("FILMOTECA TRASSIERRA");

        jLabel5.setText("Buscar Película:");

        btnTodas.setText("Listar todas");
        btnTodas.setToolTipText("Pulse para listar todas las películas disponibles");
        btnTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodasActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("FILMOTECA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCat, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .addComponent(btnCatEsp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTodas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(etiLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29))
                    .addComponent(etiWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDescrip)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btnNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCatEsp))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(etiLogo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnTodas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(etiWarning, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalir)
                    .addComponent(btnDescrip))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //METODOS DE ACCION DE LOS COMPONENTES DE LA INTERFAZ
    private void btnNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNombreActionPerformed
        abrirBusquedaPorNombre();
    }//GEN-LAST:event_btnNombreActionPerformed

    private void btnBuscarTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTituloActionPerformed
        buscarPorTitulo();
    }//GEN-LAST:event_btnBuscarTituloActionPerformed

    private void btnDescripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescripActionPerformed
        verDescripcion();
    }//GEN-LAST:event_btnDescripActionPerformed

    private void btnCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatActionPerformed
        abrirBusquedaPorCat();
    }//GEN-LAST:event_btnCatActionPerformed

    private void btnBuscarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCatActionPerformed
        buscarPorCategoria();
    }//GEN-LAST:event_btnBuscarCatActionPerformed

    private void btnCatEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatEspActionPerformed
        abrirBusquedaPorEsp();
    }//GEN-LAST:event_btnCatEspActionPerformed

    private void btnBuscarEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEspActionPerformed
        buscarPorEspeciales();
    }//GEN-LAST:event_btnBuscarEspActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodasActionPerformed
        buscarTodas();
    }//GEN-LAST:event_btnTodasActionPerformed

    private void btnSalirCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirCatActionPerformed
        busquedaCat.dispose();
    }//GEN-LAST:event_btnSalirCatActionPerformed

    private void btnSalirEspActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirEspActionPerformed
        busquedaEsp.dispose();
    }//GEN-LAST:event_btnSalirEspActionPerformed

    private void btnSalirNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirNomActionPerformed
        busquedaNombre.dispose();
    }//GEN-LAST:event_btnSalirNomActionPerformed

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
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCat;
    private javax.swing.JButton btnBuscarEsp;
    private javax.swing.JButton btnBuscarTitulo;
    private javax.swing.JButton btnCat;
    private javax.swing.JButton btnCatEsp;
    private javax.swing.JButton btnDescrip;
    private javax.swing.JButton btnNombre;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSalirCat;
    private javax.swing.JButton btnSalirEsp;
    private javax.swing.JButton btnSalirNom;
    private javax.swing.JButton btnTodas;
    private javax.swing.JDialog busquedaCat;
    private javax.swing.JDialog busquedaEsp;
    private javax.swing.JDialog busquedaNombre;
    private javax.swing.JComboBox<String> comboCat;
    private javax.swing.JComboBox<String> comboEsp;
    private javax.swing.JLabel etiCargandoTitulo;
    private javax.swing.JLabel etiLogo;
    private javax.swing.JLabel etiWarning;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabla;
    private javax.swing.JTextField txtNombrePeli;
    // End of variables declaration//GEN-END:variables
}
