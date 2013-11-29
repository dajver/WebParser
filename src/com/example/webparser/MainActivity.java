package com.example.webparser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

	// благодоря этому классу мы будет разбирать данные на куски
	public Elements title;
	// то в чем будем хранить данные пока не передадим адаптеру
	public ArrayList<String> titleList = new ArrayList<String>();
	// Listview Adapter для вывода данных
	private ArrayAdapter<String> adapter;
	// List view
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// определение данных
		lv = (ListView) findViewById(R.id.listView1);
		// запрос к нашему отдельному поток на выборку данных
		new NewThread().execute();
		// Добавляем данные для ListView
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, titleList);
	}

	/** А вот и внутрений класс который делает запросы, если вы не читали статьи у меня в блоге про отдельные
	 * потоки советую почитать */
	public class NewThread extends AsyncTask<String, Void, String> {

		// Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
		// нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
		@Override
		protected String doInBackground(String... arg) {

			// класс который захватывает страницу
			Document doc;
			try {
				// определяем откуда будем воровать данные
				doc = Jsoup.connect("http://freehabr.ru/").get();
				// задаем с какого места, я выбрал заголовке статей
				title = doc.select(".title");
				// чистим наш аррей лист для того что бы заполнить
				titleList.clear();
				// и в цикле захватываем все данные какие есть на странице
				for (Element titles : title) {
					// записываем в аррей лист
					titleList.add(titles.text());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ничего не возвращаем потому что я так захотел)
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			// после запроса обновляем листвью
			lv.setAdapter(adapter);
		}
	}
}
