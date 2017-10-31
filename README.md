# DroidCalorieCounterView
Calorie counter view for android inspired from Runtastic balance food tracker

<b><h1>About</h1></b>
An easy to implement calorie counter view with customizations.

<p align="center">
<img src="https://user-images.githubusercontent.com/12429051/32219095-0e35effe-be53-11e7-9e8b-0dc6d826cd04.png" height="450" width="275"/>
<img src="https://user-images.githubusercontent.com/12429051/32219092-0d847990-be53-11e7-822d-a6e658bd5705.png" height="450" width="275"/>
<img src="https://user-images.githubusercontent.com/12429051/32219093-0dc383a6-be53-11e7-8f91-0f2bc8faef26.png" height="450" width="275"/>
<img src="https://user-images.githubusercontent.com/12429051/32219094-0dfc8ae8-be53-11e7-95bb-5e90e1c79061.png" height="450" width="275"/>
<img src="https://user-images.githubusercontent.com/12429051/32219096-0e749c7c-be53-11e7-9980-81b46ae616c9.png" height="450" width="275"/>
</p>

<b><h1>Usage</h1></b>
<b>Gradle dependency:</b>

Add the following to your project level build.gradle:

```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add this to your app build.gradle:

```java
dependencies {
    compile 'com.github.vikramezhil:DroidCalorieCounterView:v1.0'
}
```

Add the following to the <repositories> section of your pom.xml:

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the following to the <dependencies> section of your pom.xml:

```xml
<dependency>
    <groupId>com.github.vikramezhil</groupId>
    <artifactId>DroidCalorieCounterView</artifactId>
    <version>v1.0</version>
</dependency>
```
<b><h1>Documentation</h1></b>

For a detailed documentation 📔, please have a look at the [Wiki](https://github.com/vikramezhil/DroidCalorieCounterView/wiki).

In your layout file add Droid Calorie Counter View,

```xml
<com.vikramezhil.dccv.CalorieCounterView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/calorieCounterView"
    android:layout_width="250dp"
    android:layout_height="250dp"
    app:dccvThickness="15dp"
    app:dccvHeaderTxtSize="50dp"
    app:dccvHeaderSubTxtSize="20dp"
    app:dccvFooterTxtSize="18dp"
    app:dccvFooterSubTxtSize="18dp"
    app:dccvHeaderTxt="1500"
    app:dccvHeaderSubTxt="Calories left"
    app:dccvFooterTxt="0"
    app:dccvFooterSubTxt="consumed"
    app:dccvMin="0"
    app:dccvMax="1500"
    app:dccvProgress="0"
    app:dccvClickable="true"/>
```

In your class file, initialize Droid Calorie Counter View using the ID specified in your layout file

```java
CalorieCounterView calorieCounterView = findViewById(R.id.calorieCounterView);
calorieCounterView.setIgnoreMax(true);
calorieCounterView.setDangerMaxWarning(true, Color.RED);
```

Set and implement the Droid Calorie Counter View listener method

```java
calorieCounterView.setOnCalorieCounterListener(new OnCalorieCounterListener() {
    @Override
    public void onCalorieCounterClicked() {
        Log.i(TAG, "Calorie Counter Clicked");

        // Updating the calorie counter progress
        int currentCalories = Integer.valueOf(calorieCounterView.getFooterTxt());
        calorieCounterView.setProgress(currentCalories + 100);
    }

    @Override
    public void onProgressUpdated(int progress, int remaining) {
        Log.i(TAG, "Calorie Counter progress - " + progress + ", remaining - " + remaining);

        // Setting the calorie counter header and footer
        calorieCounterView.setHeaderTxt(String.valueOf(remaining));
        calorieCounterView.setFooterTxt(String.valueOf(progress));
    }
});
```

<b><h1>License</h1></b>

Copyright 2017 Vikram Ezhil

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
