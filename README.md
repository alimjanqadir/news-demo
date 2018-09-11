# Introduction

A small demo project for recruitment.

## Architecture

Project utilises the official [Jetpack](https://developer.android.com/jetpack)
(former Architecture Components) components to implement recommended [clean app architecture](https://github.com/googlesamples/android-architecture) 
which is robust, maintainable and easy to test. To achieve offline-supported database + network 
model a repository created as single source of truth. The repository class has a direct relationship 
with viewmodel and deliver received data via LiveData observer.

![app architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

## Getting Started

Project consists of 5 packages :

* api (include retrofit service and response entity)
* data (include repository class which represents data layer)
* db (include Room database class, dao and a helper class)
* model (include a model classes that key part of the application)
* ui (includes all ui related classes activities, fragments, adapters etc.)
 
 [NewsListFragment](https://github.com/alimjanqadir/news-demo/blob/master/app/src/main/java/com/example/alimjan/news/ui/fragments/NewsListFragment.java), 
[NewsViewModel](https://github.com/alimjanqadir/news-demo/blob/master/app/src/main/java/com/example/alimjan/news/ui/viewmodels/NewsViewModel.java) 
and [NewsRepository](https://github.com/alimjanqadir/news-demo/blob/master/app/src/main/java/com/example/alimjan/news/data/NewsRepository.java)
are main three classes that consists major part of the application code. Activities are just wrapper
of fragments, there are two fragments in the application [NewsDetailFragment](https://github.com/alimjanqadir/news-demo/blob/master/app/src/main/java/com/example/alimjan/news/ui/fragments/NewsDetailFragment.java) 
is relatively simple compare to [NewsListFragment](https://github.com/alimjanqadir/news-demo/blob/master/app/src/main/java/com/example/alimjan/news/ui/fragments/NewsListFragment.java), 
they share same viewmodel to communicate with each other, when Master/Detail mode.

### Tricky Parts

1. Paging

At first paging is implemented by new [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/) 
from [Jetpack](https://developer.android.com/jetpack), but issue is it requires [PagedListAdapter](https://developer.android.com/reference/android/arch/paging/PagedListAdapter)
to show items, [PagedListAdapter](https://developer.android.com/reference/android/arch/paging/PagedListAdapter) 
uses [AsyncPagedListDiffer](https://developer.android.com/reference/android/arch/paging/AsyncPagedListDiffer) 
to perform diff operation(Just like [ListAdapter](https://developer.android.com/reference/android/support/v7/recyclerview/extensions/ListAdapter)) 
and show items according to diff operation. It sounds good but [PagedListAdapter](https://developer.android.com/reference/android/arch/paging/PagedListAdapter) 
introduces unpredictable changes to list which sometimes confuses the user. So I change my code to
handle paging with old way.

2. LiveData and Room

LiveData and Room works really well each other and makes implementing single source of truth 
principle very easy, but one thing to note that LiveData observer callback returns all the data
whenever a change occurs to the data source, which means even a simple update operation could trigger 
observer callback and return already shown data again. We should not change all the item inside the 
list just for adding and updating some data. This is done by determining what kind of data request 
causing data source to change, there are many types of requests like refresh data, load more and
delete, data handling varies depends on each request type, this is achieved by creating a flag 
to each type of request.
 
 ```java
      private void handleData(@NonNull List<News> news) {
          // ... some code is ignored
  
          // data handled differently depends on flag type
          if (mRefreshRequestFlag) {
              mRefreshRequestFlag = false;
              mAdapter.setData(news);
              // ... some code is ignored
          } else if (mLoadMoreRequestFlag) {
              mAdapter.addData(news);
              // ... some code is ignored
          } else {
              // ... some code is ignored
  
              // if request is not refresh or load more then it would be initial data from database.
              mAdapter.setData(news);
              if (isRequestResultEmpty()) {
                  refresh();
              }
          }
      }
 
 ```
 
 4. Webview receives error after page is visible
 
To prevent webview's ugly default error page to show up, webview only visible when page is visible to
user and instead of showing error page custom error message would be displayed, but after page visible
WebViewClient onReceiveError callback can be fired and the user see error message after normal page
already shown. This is weired and confuses the user so I declare a flag which indicates page already
visible or not if page visible error message would not be shown.

```java
    @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!mIsPageVisible) {
                    mIsErrorReceivedFlag = true;
                    mBinding.setNetworkState(NetworkState.error(getString(R.string.msg_request_failed)));
                }
            }
```
 
### Build

To build and run project correctly you should fulfill some requirements:

Android Studio Version: At least 3.1

Gradle Version: 4.4

Android Gradle Plugin Version: 3.1.4

Build Tools: At least 28.0.0

Compile SDK Version: 28

Target SDK Version: 28

Minimum SDK Version: 22


### Libraries

* [Jetpack Components](https://developer.android.com/jetpack/) 
    * [AppCompat Support Libraries](https://developer.android.com/topic/libraries/support-library/)
    * [Data Binding](https://developer.android.com/topic/libraries/support-library/)
    * [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle)
    * [Room](https://developer.android.com/topic/libraries/architecture/room)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
    * [Test](https://developer.android.com/topic/libraries/testing-support-library/index.html)
* [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
* [Retrofit](https://github.com/square/retrofit)
* [Rxjava](https://github.com/reactivex/rxjava)
* Javax Annotation Library (for pojo generator)


## Contact

Author: Alimjan Qadir

Email: alimjanqadir@qq.com



