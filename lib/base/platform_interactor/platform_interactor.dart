import 'package:flutter_kotlin_interaction/repository/data/lat_lng.dart';

abstract class PlatformInteractor {
  Future<void> startLocationService();
  Future<LatLng> getLocation();
}