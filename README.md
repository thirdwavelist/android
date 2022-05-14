# <img width='75px' src='app/src/main/ic_launcher-web.png' /> ThirdWaveList for Android

<a href='https://play.google.com/store/apps/details?id=com.thirdwavelist.coficiando&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' width='150px' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

[![license](https://img.shields.io/github/license/thirdwavelist/android.svg?style=flat-square)](/LICENSE) [![GitHub last commit](https://img.shields.io/github/last-commit/thirdwavelist/android.svg?style=flat-square)]()

## How to run

In order to compile and run the project you will need:
- Android Studio supporting the Gradle plugin defined [here](https://github.com/thirdwavelist/android/blob/main/gradle/libs.versions.toml#L10)
- Java 11 SDK installed

## Features:
- Fetches data from https://api.thirdwavelist.com (API key required), parsed in the common `:libraries:core` module, under the `data` layer, and presented in a RecyclerView
- Clicking on a RV item triggers a navigation action (Android Navigation Components) and opens detail view for the given item
- After the initial successful network request, the data is stored in a local database using Room
- Settings page
- `:libraries:design` module implementing Material Design guidelines
- Common abstractions in `:libraries:core-utils` and `:libraries:core` modules
- Navigation abstraction in `:libraries:navigation` module implementing Android Navigation Components
- Integrated Firebase Config for feature toggling and configuration keys

## Project overview

<img src='./dependency-graph.svg' />

## Caveats

1. The original codebase that was written >3 years ago was built on pure MVVM approach (with DataBinding and a bi-directional data flow).
This was problematic, since it didn't reflect my current thinking and its also going against some bigger trends in the industry: 
Compose (Data Binding is problematic), MVI (bi-directional data flow is problematic), so I removed the use of Data Binding, replaced it with ViewBinding,
finite View State (machine), and while I didn't really capture true unidirectional data flow (UDF), it also does not go against some of the practices
that would allow that change. Same goes with Compose; it's not integrated, but with the current architecture, it wouldn't be any blocker to do so.

## What would I do differently / improve on?

1. Change the Timber library use behind the ErrorEventLogger and BusinessEventLogger abstraction to use some actual backend/PaaS (e.g. Firebase) to
make sense of the data that's being logged

2. Add missing UI tests: as a side project, and with the one-week deadline to cover all requirements and make sure the code reflects my current 
architectural thinking, I figured it was not the smartest to invest my time on writing UI tests for these screens. I just wanted to acknowledge that.

3. More work on the presentation / repository layer to support paging from both the remote and local data sources. I didn't pick this up, since this
would have required me to touch the API (which I developed as well 3 years ago), and it wasn't the most critical aspect to get right at the start.

## License

```Text
BSD 3-Clause License

Copyright (c) 2017 - 2022, Antal János Monori & Kristoffer Tjalve
All rights reserved.
```

_See [LICENSE](/LICENSE) for more._
