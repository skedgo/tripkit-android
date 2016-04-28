# Overview
## AccountComponent
`AccountComponent` is a factory to provide `AccountService` and `UserTokenStore`. The snippet below shows how to initialize a `AccountComponent`.
```java
final AccountComponent component = DaggerAccountComponent.builder()
    .tripKit(TripKit.singleton())
    .build();
```
## AccountService
### Instantiate
```java
final AccountService accountService = component.accountService();
```
### Sign up
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