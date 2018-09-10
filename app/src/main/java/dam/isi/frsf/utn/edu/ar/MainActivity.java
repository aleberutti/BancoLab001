package dam.isi.frsf.utn.edu.ar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import ar.edu.utn.frsf.dam.bancolab01.modelo.*;

public class MainActivity extends AppCompatActivity {

    private PlazoFijo pf;
    private Cliente cliente;
    private Resources res;

    // widgets de la vista
    private Button btnHacerPlazoFijo;
    private EditText edtMonto, edtMail, edtCuit;
    private SeekBar diasSeek;
    private TextView diasPlazo, intereses, mensaje;
    private CheckBox chkAceptaTerminos;
    private RadioButton dolar, peso;
    private Switch vencimiento;
    private ToggleButton btnRenovar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res= getResources();
        pf = new PlazoFijo(res.getStringArray(R.array.tasas));
        cliente = new Cliente();

        btnHacerPlazoFijo= (Button) findViewById(R.id.btnHacerPF);
        diasPlazo= (TextView) findViewById(R.id.tvDiasSeleccionados);
        intereses= (TextView) findViewById(R.id.tvIntereses);
        mensaje = (TextView) findViewById(R.id.tvMensajes);
        diasSeek = (SeekBar) findViewById(R.id.seekDias);
        edtMonto=(EditText) findViewById(R.id.edtMonto);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtCuit = (EditText) findViewById(R.id.edtCuit);
        chkAceptaTerminos = (CheckBox) findViewById(R.id.chkAceptoTerminos);
        dolar = (RadioButton) findViewById(R.id.optDolar);
        peso = (RadioButton) findViewById(R.id.optPesos);
        vencimiento = (Switch) findViewById(R.id.swAvisarVencimiento);
        btnRenovar = (ToggleButton)findViewById(R.id.togAccion);


        btnHacerPlazoFijo.setEnabled(false);
        diasSeek.setProgress(10);
        diasSeek.setMax(180);
        diasPlazo.setText(res.getString(R.string.lblDiasSel, diasSeek.getProgress()));

        diasSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String dias = String.format(res.getString(R.string.lblDiasSel),seekBar.getProgress());
                diasPlazo.setText(dias);
                pf.setDias(i);
                if (!edtMonto.getText().toString().isEmpty()) pf.setMonto(Double.valueOf(edtMonto.getText().toString()));
                String interes = String.format(res.getString(R.string.lblInt),pf.intereses());
                intereses.setText(interes);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        chkAceptaTerminos.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    btnHacerPlazoFijo.setEnabled(true);
                }
                else{
                    btnHacerPlazoFijo.setEnabled(false);
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, res.getString(R.string.lblCondiciones), duration).show();
                }
            }
        });

        btnHacerPlazoFijo.setOnClickListener(new CompoundButton.OnClickListener(){
            @Override
            public void onClick(View v){

                if (edtMail.getText().toString().isEmpty()){
                    mensaje.setTextColor(Color.RED);
                    mensaje.setText(R.string.lblMailVacio);

                }
                else {
                    if (edtCuit.getText().toString().isEmpty()) {
                        mensaje.setTextColor(Color.RED);
                        mensaje.setText(R.string.lblCuitVacio);

                }
                    else {
                        if (edtMonto.getText().toString().isEmpty()) {
                            mensaje.setTextColor(Color.RED);
                            mensaje.setText(R.string.lblMontoVacio);

                        }
                        else {
                            if(Integer.valueOf(edtMonto.getText().toString())<0){
                                mensaje.setTextColor(Color.RED);
                                mensaje.setText(R.string.lblMonto0);
                            }
                            else {
                                if (diasSeek.getProgress() < 10) {
                                    mensaje.setTextColor(Color.RED);
                                    mensaje.setText(R.string.lblDias10);

                                } else {
                                    cliente.setMail(edtMail.getText().toString());
                                    cliente.setCuil(edtCuit.getText().toString());
                                    pf.setCliente(cliente);
                                    if (dolar.isChecked())pf.setMoneda(Moneda.DOLAR);
                                    if (peso.isChecked())pf.setMoneda(Moneda.PESO);
                                    if (vencimiento.isChecked())pf.setAvisarVencimiento(true);
                                    else pf.setAvisarVencimiento(false);
                                    if(btnRenovar.isChecked()) pf.setRenovarAutomaticamente(true);
                                    else pf.setRenovarAutomaticamente(false);
                                    pf.setMonto(Double.valueOf(edtMonto.getText().toString()));
                                    mensaje.setTextColor(Color.BLUE);
                                    mensaje.setText(pf.toString());
                                }
                            }
                        }
                    }
                }

            }
        });


    }
}
