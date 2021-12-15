package com.duckhaidoit.jt1calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private TextView txt_expression, txt_result;

    //Kết quả đã được hiển thị?
    private static boolean resultShown = false;

    //txt_result.getText()
    private String resultGetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Truy xuất 2 TextView result và expression
        txt_expression = (TextView) findViewById(R.id.txt_expression);
        txt_result = (TextView) findViewById(R.id.txt_result);

    }

    //Set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Nếu item ở menu == about -> start AboutActivity
        if (item.getItemId() == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    //Set onClick cho button C
    public void clearOnClick(View view) {
        /*
            2 chế độ:
            Short click: backspace, xóa đi kí tự cuối tính từ phải qua trái
            Long click: clear txt_result, làm mới txt_result
         */
        //Short click:
        resultGetText = txt_result.getText().toString();

        /*
            Nếu txt_result đang rỗng hoặc đang hiển thị lỗi thì đặt lại txt_result
            Nếu không thì xóa đi 1 kí tự cuối trong txt_result
         */
        if (resultGetText.equals("") || resultGetText.equals("Syntax Error") || resultGetText.equals("Infinity")) {
            txt_result.setText("");
            txt_result.setHint("0");
        } else {
            //Trả về chuỗi đã loại bỏ kí tự cuối
            String backspacedText = resultGetText.substring(0, resultGetText.length() - 1);

            if (backspacedText.equals("")) {
                txt_result.setText("");
                txt_result.setHint("0");
            } else {
                txt_result.setText(backspacedText);
            }
        }

        //Long click:
        ((Button) view).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txt_result.setText("");
                txt_result.setHint("0");
                return true;
            }
        });
    }

    //Set onClick cho button AC
    public void allClearOnClick(View view) {
        txt_result.setText("");
        txt_result.setHint("0");
        txt_expression.setText("");
        resultShown = false;
    }

    //Set onClick cho các phím số
    public void numericsOnClick(View view) {
        Button button = findViewById(view.getId());
        resultGetText = txt_result.getText().toString();

        /*
            Nếu txt_result rỗng, = 0, hoặc kết quả đã hiển thị thì khi bấm phím số sẽ nhập vào 1 biểu thức mới
            Nếu không, nối thêm số vừa nhập vào txt_result
         */
        if (resultGetText.equals("") || resultGetText.equals("0") || resultShown) {
            txt_result.setText(button.getText().toString());
            resultShown = false;
        } else {
            //Kí tự cuối txt_result
            char lastChar = resultGetText.charAt(resultGetText.length() - 1);

            //Nếu kí tự cuối là % thì thêm dấu * vào trước số
            if (lastChar == '%') {
                txt_result.append("×");
            }

            txt_result.append(button.getText());
        }
    }

    //Set onClick cho các toán tử
    public void operatorsOnClick(View view) {
        Button button = findViewById(view.getId());

        //Đặt lại resultShown
        if (resultShown) {
            resultShown = false;
        }

        resultGetText = txt_result.getText().toString();

        /*
            Nếu txt_result rỗng hoặc đang hiển thị lỗi, và toán tử vừa nhập khác căn bậc 2 thì đặt lại txt_result = 0<toán tử> (Ví dụ: "0+")
            Nếu không xét 2 TH: là căn bậc 2 và các toán tử còn lại
         */
        if (
                (resultGetText.equals("") || resultGetText.equals("Syntax Error") || resultGetText.equals("Infinity")) &&
                button.getId() != R.id.btn_root
        ) {
            txt_result.setText("0" + button.getText().toString());
        } else {
            if (button.getId() == R.id.btn_root) {
                /*
                    Nếu txt_result rỗng hoặc đang hiển thị lỗi thì in sqrt(
                    Nếu không kiểm tra xem kí tự cuối phải số không? Nếu là số thêm dấu * vào trước sqrt (VD: "...3*sqrt(")
                 */
                if (resultGetText.equals("") || resultGetText.equals("Syntax Error") || resultGetText.equals("Infinity")) {
                    txt_result.setText(button.getText().toString() + "(");
                } else {
                    char lastChar = resultGetText.charAt(resultGetText.length() - 1);

                    if (lastChar == '%' || lastChar == ')') {
                        txt_result.append("×");
                    }

                    txt_result.append(button.getText().toString() + "(");
                }
            } else {
                txt_result.append(button.getText().toString());
            }
        }
    }

    //Set onClick cho dấu chấm thập phân
    public void dotOnClick(View view) {
        resultGetText = txt_result.getText().toString();

        //Nếu kết quả đã được hiển thị thì thêm dấu "." đồng thời đặt lại resultShown = false
        if (resultShown) {
            resultShown = false;
            txt_result.setText("0.");
        } else {
            //Nếu txt_result rỗng thì đặt lại txt_result = "0."
            if (resultGetText.equals("")) {
                txt_result.setText("0.");
            } else {
                char lastChar = resultGetText.charAt(resultGetText.length() - 1);

                /*
                    Nếu kí tự cuối là toán tử "+ - * /" thì thêm "0."
                    Nếu là số thì chỉ thêm "."
                 */
                if (new String("+-×÷").indexOf(lastChar) != -1) {
                    txt_result.append("0.");
                } else if (Character.isDigit(lastChar)) {
                    txt_result.append(".");
                }
            }
        }
    }

    //Set onClick cho dấu đóng/mở ngoặc
    public void bracketsOnClick(View view) {
        Button button = findViewById(view.getId());

        resultGetText = txt_result.getText().toString();

        //Nếu txt_result rỗng hoặc đang hiển thị lỗi thì chỉ cần thêm dấu đóng/mở ngoặc
        if (resultGetText.equals("") || resultGetText.equals("Syntax Error") || resultGetText.equals("Infinity")) {
            txt_result.append(button.getText().toString());
        } else {
            char lastChar = resultGetText.charAt(resultGetText.length() - 1);

            /*
                Nếu kí tự cuối là số hoặc đóng ngoặc và button là mở ngoặc thì thêm "*("
                Nếu không thêm dấu đóng/mở ngoặc như thường
             */
            if ((Character.isDigit(lastChar) || lastChar == ')') && button.getId() == R.id.btn_openingBrace) {
                txt_result.append("×" + button.getText().toString());
            } else {
                txt_result.append(button.getText().toString());
            }
        }
    }

    //Set onClick cho phím bằng
    public void equalOnClick(View view) {
        //Di chuyển result_text -> expression_text
        Animation textTransition = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_transition);
        txt_expression.startAnimation(textTransition);

        String expression = txt_result.getText().toString();
        Double result = new ExpressionCalculation(expression).calculate();

        //Nếu kết quả không phải là số thì hiển thị "Syntax Error" (Cú pháp lỗi)
        if (Double.isNaN(result)) {
            txt_result.setText("Syntax Error");
            txt_expression.setText("");
        } else {
            try {
                //Dùng BigDecimal để hiển thị chính xác hơn
                BigDecimal bd_result = BigDecimal.valueOf(result);

                txt_expression.setText(txt_result.getText().toString() + " =");

                /*
                    Nếu số có độ dài số nhỏ hơn 16 thì để nguyên
                    Nếu lớn hơn dùng dạng double có E để biểu diễn
                 */
                if (bd_result.compareTo(BigDecimal.valueOf(1E+16)) > 0) {
                    txt_result.setText(bd_result.stripTrailingZeros().toString());
                } else {
                    txt_result.setText(bd_result.stripTrailingZeros().toPlainString());
                }
            } catch (Exception e) {
                //Infinity
                txt_result.setText(result.toString());
                txt_expression.setText("");
            }
        }

        //Kết quả đã được hiển thị
        resultShown = true;
    }
}