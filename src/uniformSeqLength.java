import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.DNACompoundSet;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.*;

public class uniformSeqLength {

    public static void main(String[] args) throws Exception{
		/*
		 * Method 1: With the FastaReaderHelper
		 */
        //Try with the FastaReaderHelper
        LinkedHashMap<String, DNASequence> a = FastaReaderHelper.readFastaDNASequence(new File(args[0]));

        //1.) Iterate over each sequence and FIND the minimum length sequence
        int mindnalength = 2147483647; //hack
        int maxdnalength = 0;
        for (  Entry<String, DNASequence> entry : a.entrySet() ) {
            if(entry.getValue().getLength()<mindnalength) {
                mindnalength=entry.getValue().getLength();
            }
            if(entry.getValue().getLength()>maxdnalength) {
                maxdnalength=entry.getValue().getLength();
            }
        }


        //2.a) Iterate over each sequence and SET each seqeuence to a substring of the original for uniform length
        Iterator<Map.Entry<String, DNASequence>> iterator = a.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, DNASequence> entry = iterator.next();
            entry.setValue(new DNASequence(entry.getValue().toString().substring(0,mindnalength)));//getValue().getSubSequence(11,100).toString()));
            //System.out.print(">");
            //System.out.println(entry.getKey());
            //System.out.println(entry.getValue());
        }

        //2.b) Iterate over each sequence and SET each sequence to a superstring of the original with additional N's
        Iterator<Map.Entry<String, DNASequence>> iterator = a.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, DNASequence> entry = iterator.next();
            //entry.setValue(new DNASequence(entry.getValue().toString().substring(0,mindnalength)));//getValue().getSubSequence(11,100).toString()));
            //if length less than maxdnalength,
            // calculate how much less and store in a temp string
            // concatenate temp string to the end of the entry string
        }


        //3.a) Write the trimmed sequences to file
        //FileOutputStream out = new FileOutputStream(args[1]);
        //new FastaWriter(out, a.values(),a.keySet());
        //FastaWriterHelper.writeNucleotideSequence(out, a.values());
        try {
            FileWriter fstream = new FileWriter(args[1], true);
            BufferedWriter out = new BufferedWriter(fstream);


            Iterator<Map.Entry<String, DNASequence>> iterator2 = a.entrySet().iterator();
            while(iterator2.hasNext()) {
                Map.Entry<String, DNASequence> entry = iterator2.next();
                out.write(">");
                out.write(entry.getKey().toString());
                out.newLine();
                out.write(entry.getValue().toString());
                out.newLine();
            }

            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


		/*
		 * Method 2: With the FastaReader Object

        //Try reading with the FastaReader
        FileInputStream inStream = new FileInputStream( args[0] );
        FastaReader<ProteinSequence,AminoAcidCompound> fastaReader =
                new FastaReader<ProteinSequence,AminoAcidCompound>(
                        inStream,
                        new GenericFastaHeaderParser<ProteinSequence,AminoAcidCompound>(),
                        new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
        LinkedHashMap<String, ProteinSequence> b = fastaReader.process();
        for (  Entry<String, ProteinSequence> entry : b.entrySet() ) {
            System.out.println( entry.getValue().getOriginalHeader() + "=" + entry.getValue().getSequenceAsString() );
        }*/

    }

}
