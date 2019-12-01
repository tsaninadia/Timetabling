import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.LinkedList;

public class Jadwal {
    int ts[][];
    int cm[][];
    int siswa;
    String file;
    public Jadwal(int timeslot[][], int matrix[][], int peserta, String nama)
    {
        ts = timeslot;
        cm = matrix;
        siswa = peserta;
        file = nama;
    }
    public void print()
    {
        for(int i=0; i<ts.length; i++) {
            for (int j = 0; j < ts[i].length; j++)
            {
                System.out.print(ts[i][j] + " ");
            }
            System.out.println();
        }
    }
    public double countPenalty()
    {
        double penalty = 0;
        for(int i=0; i<cm.length-1; i++)
        {
            for(int j=i+1; j<cm.length; j++)
            {
                if(cm[i][j]!=0)
                {
                    if(Math.abs(ts[j][1]-ts[i][1])>=1 && Math.abs(ts[j][1]-ts[i][1])<=5)
                    {
                        penalty = penalty + (cm[i][j]*(Math.pow(2, (5-(Math.abs(ts[j][1]-ts[i][1]))))));
                    }
                }
            }
        }
        return penalty/siswa;
    }

    public int getTimeslot()
    {
        int tt = 0;
        for(int i=0; i<ts.length; i++)
            if(ts[i][1]>tt)
                tt = ts[i][1];
        return tt+1;
    }

    public void Simulated()
    {
        int a[] = new int[ts.length];
        double S = countPenalty();
        double s = S;
        for(int i=0; i<a.length; i++)
            a[i] = ts[i][1];
        double T = 1.0000000;
        //int llh = (int) (Math.random()*4);

        //int tabu [] = new int [3];
        LinkedList<Integer> tabu = new LinkedList<Integer>();
        int llh;
        for(int i=0; i<1000; i++)
        {
            do {
                llh = (int) (Math.random()*6);
            }while(tabu.contains(llh));
            if(llh == 0)
                move(1);
            if(llh == 1)
                swap(2);
            if(llh == 2 )
                swap(3);
            if(llh == 3 )
                move(2);
            if(llh == 4 )
                move(3);
            if(llh == 5 )
                move(4);

            if(safeAll())
            {
                s = countPenalty();
                if(s<=S)
                {
                    //System.out.println("Iterasi ke " + (i+1) + " aman dan penaltinya " + S);
                    S=s;
                    for (int j = 0; j <a.length; j++)
                        a[j] = ts[j][1];
                }
                else
                {

                    double prob = (double) (Math.exp(-(Math.abs(s-S)/T)));
                    double random = (double) (Math.random());
                    if(random<prob)
                    {
                        System.out.println(prob + " " + random);
                        S=s;
                        for (int j = 0; j <a.length; j++)
                            a[j] = ts[j][1];
                    }
                    else
                    {
                        if(tabu.size()==3)
                        {
                            tabu.removeFirst();
                            tabu.add(llh);
                        }
                        else tabu.add(llh);
                        System.out.println(prob + " " + random);
                        for (int j = 0; j < ts.length; j++)
                            ts[j][1] = a[j];
                    }

                }
            }
            else
            {
                if(tabu.size()==3)
                {
                    tabu.removeFirst();
                    tabu.add(llh);
                }
                //System.out.println("Iterasi ke " + (i+1) + " tidak aman dan penaltinya " + S);
                for (int j = 0; j < ts.length; j++)
                    ts[j][1] = a[j];
            }
            System.out.println("Iterasi ke " + (i+1) +" penaltinya = " + S);
            T = T*0.99;
        }

        for(int i=0; i<ts.length; i++) {
            ts[i][1] = a[i];
        }
        System.out.println("Hasil Simulated " + countPenalty());
        System.out.println("ukuran tabu " + tabu.size());
        System.out.println(tabu);
    }

    public boolean safeAll()
    {
        for(int i=0; i<cm.length; i++)
            for(int j=0; j<cm[i].length; j++)
                if(cm[i][j]!=0 && ts[i][1]==ts[j][1])
                    return false;
        return true;
    }

    public void move(int x)
    {
        int indeks [][] = new int[x][2];
        for(int i=0; i<indeks.length; i++)
        {
            indeks[i][0] = (int) (Math.random()*cm.length);
            indeks[i][1] = (int) (Math.random()*getTimeslot());
        }
        /*for(int i=0; i<indeks.length; i++)
        {
            for(int j=0; j<indeks[i].length; j++)
            {
                System.out.print(indeks[i][j] + " ");
            }
            System.out.println();
        }*/
        for(int i=0; i<indeks.length; i++)
            ts[indeks[i][0]][1] = indeks[i][1];
    }

    public void swap(int x)
    {
        int temp;
        int indeks[] = new int [x];
        for (int i=0; i<indeks.length; i++)
            indeks[i] = (int) (Math.random()*cm.length);
        //for(int i=0; i<indeks.length; i++)
          //  System.out.println(indeks[i]);
        temp = ts[indeks[indeks.length-1]][1];
        for(int i=indeks.length-1; i>0; i--)
            ts[i][1] = ts[i-1][1];
        ts[indeks[0]][1] = temp;
    }

    public void LAHC()
    {
        int a[] = new int[ts.length];
        double S = countPenalty();
        double s = S;
        for(int i=0; i<a.length; i++)
            a[i] = ts[i][1];
        double fitness[] = new double[200];
        for(int i=0; i<fitness.length;i++)
            fitness[i] = S;
        double idle = 0;
        LinkedList<Integer> tabu = new LinkedList<Integer>();
        int llh;
        for(int i=0; i<1000000; i++)
        {
            do {
                llh = (int) (Math.random()*5);
            }while(tabu.contains(llh));
            if(llh == 0)
                move(1);
            if(llh == 1)
                swap(2);
            if(llh == 2 )
                move(2);
            if(llh == 3 )
                swap(3);
            if(llh == 4 )
                move(3);
            if(safeAll())
            {
                s = countPenalty();
                if(s>=S)
                {
                    idle = idle + 1;
                    if(idle>1000000 *0.02)
                        break;
                }
                else
                    idle = 0;
                int v = i%fitness.length;
                if(s<=S || s<fitness[v])
                {
                    S=s;
                    for (int j = 0; j <a.length; j++)
                        a[j] = ts[j][1];
                }
                else
                {
                    if(tabu.size()==2)
                    {
                        tabu.removeFirst();
                        tabu.add(llh);
                    }
                    else tabu.add(llh);
                    for (int j = 0; j < ts.length; j++)
                        ts[j][1] = a[j];
                }
                if(S<fitness[v])
                {
                    fitness[v] = S;
                }
            }
            else
            {
                if(tabu.size()==2)
                {
                    tabu.removeFirst();
                    tabu.add(llh);
                }
                else tabu.add(llh);
                for (int j = 0; j < ts.length; j++)
                    ts[j][1] = a[j];
            }
            System.out.println("Iterasi ke " + (i+1) +" penaltinya = " + S);
        }

        System.out.println("Hasil LAHC Penalty = " + countPenalty());
        System.out.println("Hasil LAHC S = " + S);
        TextFileWritingExample1();
    }

    public void HC()
    {
        int a[] = new int[ts.length];
        double S = countPenalty();
        double s = S;
        for(int i=0; i<a.length; i++)
            a[i] = ts[i][1];
        LinkedList<Integer> tabu = new LinkedList<Integer>();
        int llh;
        for(int i=0; i<1000000; i++)
        {
            do {
                llh = (int) (Math.random()*6);
            }while(tabu.contains(llh));
            if(llh == 0)
                move(1);
            if(llh == 1)
                swap(2);
            if(llh == 2 )
                swap(3);
            if(llh == 3 )
                move(2);
            if(llh == 4 )
                move(3);
            if(llh == 5 )
                move(4);
            if(safeAll())
                {
                s = countPenalty();
                if(s<=S)
                {
                   //System.out.println("Iterasi ke " + (i+1) + " aman dan penaltinya " + S);
                    S=s;
                    for (int j = 0; j <a.length; j++)
                    a[j] = ts[j][1];
                }
                else
                {
                    if(tabu.size()==3)
                    {
                        tabu.removeFirst();
                        tabu.add(llh);
                    }
                    else tabu.add(llh);
                    //System.out.println("Iterasi ke " + (i+1) + " aman dan penaltinya " + S);
                    for (int j = 0; j < ts.length; j++)
                        ts[j][1] = a[j];
                }
            }
            else
            {
                if(tabu.size()==3)
                {
                    tabu.removeFirst();
                    tabu.add(llh);
                }
                else tabu.add(llh);
                //System.out.println("Iterasi ke " + (i+1) + " tidak aman dan penaltinya " + S);
                for (int j = 0; j < ts.length; j++)
                    ts[j][1] = a[j];
            }
            System.out.println("Iterasi ke " + (i+1) +" penaltinya = " + S);

        }
        for(int i=0; i<ts.length; i++) {
            ts[i][1] = a[i];
        }

        //print();
        System.out.println(tabu);
        System.out.println("Hasil HC " + countPenalty());
        //TextFileWritingExample1();
    }

    public void TextFileWritingExample1() {
        try {
            FileWriter writer = new FileWriter(file + ".sol", true);
            for (int i = 0; i <ts.length; i++) {
                for (int j = 0; j <ts[i].length; j++) {
                    writer.write(ts[i][j]+ " ");
                }
                //this is the code that you change, this will make a new line between each y value in the array
                writer.write("\n");   // write new line
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cek()
    {
        int a [] = new int[ts.length];
        for(int i=0; i<a.length; i++)
            a[i] = ts[i][1];
        for(int i=0; i<1000000; i++)
        {
            swap(2);
            if (safeAll()) {
                System.out.println("Iterasi ke " + (i + 1) + " sukses");
                for (int j = 0; j <a.length; j++)
                    a[j] = ts[j][1];
                break;
            } else {
                System.out.println("Iterasi ke " + (i + 1) + " gagal");
                for (int j = 0; j < ts.length; j++)
                    ts[j][1] = a[j];
            }
        }
        for(int i=0; i<ts.length; i++)
            ts[i][1] = a[i];
    }
}
