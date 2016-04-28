# Overview
## AccountComponent
`AccountComponent` is a factory to provide `AccountService` and `UserTokenStore`. The snippet below shows how to initialize a `AccountComponent`.
```java
final AccountComponent component = DaggerAccountComponent.builder()
    .tripKit(TripKit.singleton())
    .build();
```
## UserTokenStore
`UserTokenStore` is a helper to retrieve and to persist user token obtained via [Account APIs](http://planck.buzzhives.com/tripgodata/account/resource_AccountSpecificRestService.html). You might need to supply `UserTokenStrore` to `Configs.userTokenProvider()` so that TripKit can send appropriate booking requests. As of now, the implementation is that TripKit will attach user token (if present) in the headers of all the requests that come out of it.
### Instantiate
```java
final UserTokenStore userTokenStore = component.userTokenStore();
```
## AccountService
### Instantiate
```java
final AccountService accountService = component.accountService();
```
### Sign up
After signing up successfully, the user token from the response will be persisted on disk. In order to access the token, invoke `UserTokenStore.call()`.
```java
@Test public void signUp() {
  accountService.signUpAsync(
      ImmutableSignUpBody.builder()
          .name("Thuy") // Optional.
          .username("thuy@skedgo.com")
          .password("qwertyuiop")
          .build()
  ).subscribe(new Action1<SignUpResponse>() {
    @Override public void call(SignUpResponse response) {
      // Successfully signed up.
    }
  }, new Action1<Throwable>() {
    @Override public void call(Throwable error) {
      // Failed to sign up.
    }
  });
}
```
### Log in
After logging in successfully, the user token from the response will be persisted on disk. In order to access the token, invoke `UserTokenStore.call()`.
```java
@Test public void logIn() {
  accountService.logInAsync(
      ImmutableLogInBody.builder()
          .username("thuy@skedgo.com")
          .password("qwertyuiop")
          .build()
  ).subscribe(new Action1<LogInResponse>() {
    @Override public void call(LogInResponse response) {
      // Logged in successfully.
    }
  }, new Action1<Throwable>() {
    @Override public void call(Throwable error) {
      // Failed to log in.
    }
  });
}
```
### Log out
After logging out, the persistent user token on disk will be deleted. `UserTokenStore.call()` will return null.
```java
@Test public void logOut() {
  accountService.logOutAsync().subscribe(new Action1<LogOutResponse>() {
    @Override public void call(LogOutResponse response) {
      // Successfully logged out.
    }
  }, new Action1<Throwable>() {
    @Override public void call(Throwable error) {
      // Failed to log out.
    }
  });
}
```