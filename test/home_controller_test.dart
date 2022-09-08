import 'package:flutter_kotlin_interaction/home/controller/home_controller_impl.dart';
import 'package:flutter_kotlin_interaction/home/controller/success_home_state.dart';
import 'package:flutter_kotlin_interaction/repository/data/lat_lng.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';

import 'mocks.dart';

void main() {
  test('''
  WHEN PlatformInteractor is successful
  THEN HomeController returns a valid SuccessState with LatLng
  ''', () async {
    // WHEN
    final platformInteractorMock = MockPlatformInteractor();
    when(() => platformInteractorMock.startLocationService()).thenAnswer(
      (_) async {
        return;
      },
    );
    when(() => platformInteractorMock.getLocation()).thenAnswer(
      (_) async {
        return LatLng(
          lat: 9.9,
          lng: 9.9,
        );
      },
    );

    // THEN
    final controller = HomeControllerImpl(
      platformInteractorMock,
    );
    await controller.loadLocation();
    final actual = controller.state;
    final expected = SuccessHomeState(
      latLng: LatLng(
        lat: 9.9,
        lng: 9.9,
      ),
      loading: false,
    );
    // ASSERT
    expect(actual, expected);
  });
}
