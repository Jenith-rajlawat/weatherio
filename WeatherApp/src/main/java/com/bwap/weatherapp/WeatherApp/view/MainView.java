package com.bwap.weatherapp.WeatherApp.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringUI(path= "")//Basically saying that to take this as the main page to show the user
public class MainView extends UI{
	
	@Autowired
	private WeatherService weatherService;
	
	private VerticalLayout mainLayout;
	private NativeSelect<String> unitSelect;
	private TextField cityTextField;
	private Button searchButton;
	private HorizontalLayout dashBoard;
	private Label location;
	private Label currentTemp;
	private HorizontalLayout mainDescriptionLayout;
	private Label weatherDescription;
	private Label maxWeather;
	private Label minWeather;
	private Label humidity;
	private Label pressure;
	private Label wind;
	private Label feelsLike;
	private Image iconImg;
	
	
	
	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		
		mainLayout();
		setHeader();
		setLogo();
		setForm();
		dashBoardTitle();
		dashBoardDetails();
		searchButton.addClickListener(clickEvent -> {
			if(!cityTextField.getValue().equals("")) {
				try {
					updateUI();
				}
				catch(JSONException e) {
					e.printStackTrace();
				}
			}
			else {
				Notification.show("Please enter the city's name");
			}
			
		});
	}

	

	


	private void mainLayout() {
		
		iconImg=new Image();
		mainLayout = new VerticalLayout(); // Initialize mainLayout
		mainLayout.setWidth("100%");
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		setContent(mainLayout);
	}

	private void setHeader() {
		HorizontalLayout header=new HorizontalLayout();
		header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label title = new Label("Weather App");
		Label creator=new Label("-Jenith Rajlawat");
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		
		header.addComponent(title);
		header.addComponent(creator);
		
		mainLayout.addComponent(header);
	}
	
	private void setLogo() {
		HorizontalLayout logo= new HorizontalLayout();
		logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Image img=new Image(null,new ClassResource("/static/Logo.png")); 
		logo.setWidth("100%");
		logo.setHeight("100%");
		
		logo.addComponent(img);
		mainLayout.addComponent(logo);
	}
	
	private void setForm() {
		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		
		//Selection Component 
		unitSelect =new NativeSelect<>();
		ArrayList<String> items =new ArrayList<>();
		items.add("C");
		items.add("F");
		
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		
		formLayout.addComponent(unitSelect);
		
		//CityTextField
		cityTextField= new TextField();
		cityTextField.setWidth("80%");
		formLayout.addComponent(cityTextField);
	
		//Search Button
		searchButton=new Button();
		searchButton.setIcon(VaadinIcons.SEARCH);
		formLayout.addComponent(searchButton);
		
		
		
		mainLayout.addComponents(formLayout);
		
		
		
	}
	
	
	private void dashBoardTitle() {
		
		 dashBoard=new HorizontalLayout(); 
		 dashBoard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		 //city location
		 
		 location= new Label("Currently in Bhakkhar");
		 location.addStyleName(ValoTheme.LABEL_H2);
		 location.addStyleName(ValoTheme.LABEL_LIGHT);
		 
		 
		 //current Temperature
		 currentTemp= new Label("10C");
		 currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
		 currentTemp.setStyleName(ValoTheme.LABEL_H1);
		 
		 dashBoard.addComponents(location,iconImg,currentTemp);
		 mainLayout.addComponents(dashBoard);
		 

	}
	
	private void dashBoardDetails() {
		mainDescriptionLayout=new HorizontalLayout();
		mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		
		//description layout
		VerticalLayout descriptionLayout =new VerticalLayout();
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//Weather Description
		weatherDescription =new Label("Desciption: Clear Skies");
		weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
		descriptionLayout.addComponent(weatherDescription);
		
		//min weather
		minWeather =new Label("Min:53");
		descriptionLayout.addComponent(minWeather);
		
		//max weather
		maxWeather =new Label("Max:59");
		descriptionLayout.addComponent(maxWeather);
		
		//Different Column for pressure and humidity
		VerticalLayout pressureLayout =new VerticalLayout();
		pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		pressure =new Label("Pressure: 231Pa");
		pressureLayout.addComponents(pressure);
		
		humidity =new Label("Humidity: 231");
		pressureLayout.addComponents(humidity);
		
		wind =new Label("Wind Speed: 231");
		pressureLayout.addComponents(wind);
		
		feelsLike =new Label("Feels like: 231");
		pressureLayout.addComponents(feelsLike);
		
		
		mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
		
	}
	private void updateUI() {
		// TODO Auto-generated method stub
		String city =cityTextField.getValue();
		String defaultUnit;
		weatherService.setCityName(city);
		
		if(unitSelect.getValue().equals("F")) {
			weatherService.setUnit("imperials");
			unitSelect.setValue("F");
			defaultUnit="\u00b0"+"F";
		}
		else {
			weatherService.setUnit("metric");
			defaultUnit="\u00b0"+"C";
			unitSelect.setValue("C");
		}
		
		
		
		location.setValue("Currently in "+ city);
		JSONObject mainObject= weatherService.returnMain();
		int temp=mainObject.getInt("temp");
		currentTemp.setValue(temp+defaultUnit);
		
		
		//getting icon from API
		
		String iconCode=null;
		String weatherDescriptionNew=null;
		JSONArray jsonArray= weatherService.returnWeatherArray();
		for(int i=0;i<jsonArray.length();i++) {
			JSONObject weatherObj=jsonArray.getJSONObject(i);
			iconCode=weatherObj.getString("icon");
			weatherDescriptionNew=weatherObj.getString("description");
			System.out.println(weatherDescriptionNew);
		
		}
	iconImg.setSource(new ExternalResource("https://openweathermap.org/img/wn/"+iconCode+"@2x.png"));
		
	weatherDescription.setValue("Description: "+ weatherDescriptionNew);
	minWeather.setValue("Min Temp: " +weatherService.returnMain().getInt("temp_min")+unitSelect.getValue());
	maxWeather.setValue("Max Temp: " +weatherService.returnMain().getInt("temp_max")+unitSelect.getValue());
	pressure.setValue("Pressure: "+weatherService.returnMain().getInt("pressure"));
	humidity.setValue("Humidity: "+weatherService.returnMain().getInt("humidity"));
	wind.setValue("Wind speed: "+weatherService.returnWind().getInt("speed"));
	feelsLike.setValue("Feels like: "+weatherService.returnMain().getDouble("feels_like"));
	
	
	
	
	mainLayout.addComponents(dashBoard,mainDescriptionLayout);
	}


	
}
