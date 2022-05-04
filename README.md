# Newsnest


## About

News app for Android devices.

## What's new in v2
* Multilingual news support (Hindi and English)
* Notifications
* Bug fixes
* User Settings are now stored in SharedPreferences instead of local SQLite Database.

## Features

* User can read news headlines from all over the world.
* User can search news by region (Global and India) and categories (Tech, entertainment, sports etc.)
* User can search news by typing keyword and sort articles by newest, popularity and relevancy in the search news section.
* User can save any article locally.
* Both Dark and Light theme are available.


## Permissions Required

* Internet
* Network State
* Vibrate

## Building the Application
Get your API Key from newspi.org and then place the value inside gradle.properties like this:
```
API_KEY="your_api_key"
```


## Technologies Used
  
  * [Kotlin](https://kotlinlang.org/docs/home.html)
  * [Retrofit](https://square.github.io/retrofit/)
  * [Room](https://developer.android.com/jetpack/androidx/releases/room)
  * [Coroutines](https://developer.android.com/kotlin/coroutines)
  * [Glide](https://github.com/bumptech/glide)
  * [Gson](https://github.com/google/gson)
  * [News API](https://newsapi.org/)
  * [Google ML Kit](https://newsapi.org/)
  * [Jsoup](https://jsoup.org/)
  * [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager)
  * [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
___

## Compatibilty 

* Android (API level 16) and higher
___


## Licensing

* Newsnest is licensed under the MIT License. See [LICENSE](https://github.com/sreshtha10/Newsnest/blob/master/LICENSE) for the full license text.
