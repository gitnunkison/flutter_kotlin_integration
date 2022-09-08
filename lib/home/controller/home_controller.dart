import 'dart:developer' as developer;

import 'package:flutter/services.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

import '../../base/base_state.dart';
import '../../base/error_state.dart';
import '../../repository/data/lat_lng.dart';
import 'success_home_state.dart';

class HomeController extends StateNotifier<BaseState> {
  LatLng _lastLatLng = LatLng.empty();

  HomeController() : super(SuccessHomeState.initial()) {
    platform.invokeMethod('startLocationService').then((value) {
      state = SuccessHomeState(
        latLng: LatLng.empty(),
        message: "Serviço de captura de localização inicializado.",
        loading: false,
      );
    }, onError: (_) {
      state = ErrorState(
        message: "Erro ao iniciar serviço de localização.",
      );
    });
  }

  void loadLocation() async {
    _setLoadingState();
    try {
      final location = await platform.invokeMethod('getLocation');
      _lastLatLng = LatLng(
        lat: location[0],
        lng: location[1],
      );
      state = SuccessHomeState(
        latLng: _lastLatLng,
        loading: false,
      );
    } on PlatformException catch (e) {
      developer.log(e.stacktrace.toString());
      state = ErrorState(
        message: e.message ?? "Erro ao capturar a localização",
      );
    }
  }

  void _setLoadingState() {
    state = SuccessHomeState(
      latLng: _lastLatLng,
      loading: true,
    );
  }

  static const platform = MethodChannel('com.nunkison.flutter/location');

  bool isLoading() =>
      state is SuccessHomeState && (state as SuccessHomeState).loading;
}
