import 'dart:developer' as developer;

import 'package:flutter/services.dart';
import 'package:flutter_kotlin_interaction/base/platform_interactor/platform_interactor.dart';

import '../../base/base_state.dart';
import '../../base/error_state.dart';
import '../../repository/data/lat_lng.dart';
import 'home_controller.dart';
import 'success_home_state.dart';

class HomeControllerImpl extends HomeController<BaseState> {
  LatLng _lastLatLng = LatLng.empty();
  final PlatformInteractor platformInteractor;

  HomeControllerImpl(
    this.platformInteractor,
  ) : super(SuccessHomeState.initial()) {
    platformInteractor.startLocationService().then((value) {
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

  @override
  Future<void> loadLocation() async {
    _setLoadingState();
    try {
      _lastLatLng = await platformInteractor.getLocation();
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

  @override
  bool isLoading() =>
      state is SuccessHomeState && (state as SuccessHomeState).loading;
}
