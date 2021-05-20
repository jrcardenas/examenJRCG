package accesodatos3;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility
 *
 * @author JAVIER 
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            //He tenido que introducir manualmente el acceso al archivo de configuracion porque daba error.
            sessionFactory = new AnnotationConfiguration().configure("/accesodatos3/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Lanza mensaje de excepcion
            System.err.println("Fallo al crear sesión. " + ex);
            System.out.println("\nHa ocurrido un error al conectar con MYSQL."
                    + "\nAsegúrese de que el servidor está escuchando peticiones. El programa se cerrará.\n");
            //Inserto salida del programa porque el programa no arranca pero tampoco sale si lo dejo tal cual
            System.exit(2);
            throw new ExceptionInInitializerError(ex.toString());
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
