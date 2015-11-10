package br.com.poc.slos.endpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.softlayer.objectstorage.ObjectFile;

/**
 * @author decarli
 * @version 1.0
 * @since 10/11/15.
 */
@Path( "fileupload" )
public class FileUploadEndpoint {

    private String baseUrl = "";
    private String user = "";
    private String password = "";
    private String location = "";

    @POST
    @Consumes( "multipart/form-data" )
    public Response fileUpload( MultipartFormDataInput input ) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // Get file data to save
        List<InputPart> inputParts = uploadForm.get( "attachment" );

        for ( InputPart inputPart : inputParts ) {
            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName( header );

                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody( InputStream.class,
                            null );

                byte[] bytes = IOUtils.toByteArray( inputStream );

                String extensao = fileName.substring( fileName.lastIndexOf( "." ) );

                String nomeArquivo = sendFileToObjectStore( bytes, extensao );


                System.out.println( "File Name: " + nomeArquivo );


                return Response.status( 200 ).entity( "Uploaded file name : " + fileName )
                            .build();

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return null;

    }

    private String sendFileToObjectStore( byte[] bytes, String extensao ) throws IOException, EncoderException {
        String randomNameFile = UUID.randomUUID().toString() + extensao;

        ObjectFile ofile = new ObjectFile( randomNameFile,
                    location, baseUrl, user, password, true );
        Map<String, String> tags = new HashMap<String, String>();
        tags.put( "testtag", "testvalue" );

        //TODO criar método que receba um byte[] representando o arquivo a ser armazenado
        String etag = ofile.uploadFile( bytes, tags );
        System.out.println( "TAG: " + etag );

        return randomNameFile;
    }

    private String getFileName( MultivaluedMap<String, String> header ) {

        String[] contentDisposition = header.getFirst( "Content-Disposition" ).split( ";" );

        for ( String filename : contentDisposition ) {
            if ( ( filename.trim().startsWith( "filename" ) ) ) {

                String[] name = filename.split( "=" );

                String finalFileName = name[ 1 ].trim().replaceAll( "\"", "" );
                return finalFileName;
            }
        }
        return "unknown";
    }

    // Utility method
    private void writeFile( byte[] content, String filename ) throws IOException {
        File file = new File( filename );
        if ( !file.exists() ) {
            System.out.println( "not exist> " + file.getAbsolutePath() );
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream( file );
        fop.write( content );
        fop.flush();
        fop.close();
    }
}
