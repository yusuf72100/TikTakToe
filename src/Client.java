import java.rmi.Naming;

public class Client {
    public static void sendData(int position){
        try{
            PositionInterface pos = (PositionInterface) Naming.lookup("rmi://localhost:1099/Position");
            int result = pos.position (position);
            System.out.println ("Datas : " + result);
        }
        catch (Exception e)
        {
            System.out.println ("Erreur d'accès à l'objet distant.");
            System.out.println (e.toString());
        }
    }
}
