# Overview
## BookingComponent
`BookingComponent` is a factory to provide `AuthService`, `QuickBookingApi` and `BookingApi`. The snippet below shows how to initialize a `BookingComponent`.
```java
final BookingComponent component = DaggerBookingComponent.builder()
    .tripKit(TripKit.singleton())
    .build();
```
## AuthService
### Instantiate
```java
final AuthService authService = component.authService();
```
### Fetch a list of available providers for a region
```java
// Assume we would like to retrieve providers for Denver, US.
final Location somewhereInDenverUS = new Location(39.740774, -104.993161);
TripKit.singleton().getRegionService()
    .getRegionByLocationAsync(somewhereInDenverUS)
    .flatMap(new Func1<Region, Observable<List<AuthProvider>>>() {
      @Override public Observable<List<AuthProvider>> call(Region region) {
        return authService.fetchProvidersByRegionAsync(region);
      }
    })
    .subscribe(new Action1<List<AuthProvider>>() {
      @Override public void call(List<AuthProvider> providers) {
        // Successfully fetched providers.
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable error) {
        // Failed to retrieve providers.
      }
    });
```
`List<AuthProvider>` returned represents following JSON structure:
```json
[
  {
    "provider": "uber",
    "action": "signin",
    "url": "https://granduni.buzzhives.com/satapp-beta/auth/uber/signin",
    "status": "Not setup"
  },
  {
    "provider": "lyft",
    "action": "logout",
    "url": "https://granduni.buzzhives.com/satapp-beta/auth/lyft/logout",
    "status": "Logged in"
  }
]
```
## QuickBookingApi
### Instantiate
```java
final QuickBookingApi quickBookingApi = component.quickBookingApi();
```
### Fetch quick bookings
```java
final String quickBookingsUrl = segment.getBooking().getQuickBookingsUrl();
if (quickBookingsUrl != null) {
  quickBookingApi.fetchQuickBookingsAsync(quickBookingsUrl)
      .subscribe(new Action1<List<QuickBooking>>() {
        @Override public void call(List<QuickBooking> bookings) {
          // Successfully fetched quick booking options.
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable error) {
          // Failed to fetch quick bookings.
        }
      });
}
```