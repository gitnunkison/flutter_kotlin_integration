import 'package:flutter/services.dart';
import 'package:flutter_kotlin_interaction/base/platform_interactor/platform_interactor.dart';
import 'package:flutter_kotlin_interaction/repository/data/lat_lng.dart';

class PlatformInteractorImpl extends PlatformInteractor{

  static const platform = MethodChannel('com.nunkison.flutter/location');

  @override
  Future<void> startLocationService() async {
    await platform.invokeMethod('startLocationService');
    return;
  }

  @override
  Future<LatLng> getLocation() async {
    final location = await platform.invokeMethod('getLocation');
    return LatLng(
      lat: location[0],
      lng: location[1],
    );
  }
}