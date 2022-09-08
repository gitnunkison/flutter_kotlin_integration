import '../../base/base_state.dart';
import '../../repository/data/lat_lng.dart';

class SuccessHomeState extends BaseState {
  final LatLng latLng;
  final bool loading;

  SuccessHomeState({
    required this.latLng,
    required this.loading,
    message,
  }) : super(message: message);

  static SuccessHomeState initial() => SuccessHomeState(
        latLng: LatLng.empty(),
        loading: false,
      );

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is SuccessHomeState &&
          runtimeType == other.runtimeType &&
          latLng == other.latLng &&
          loading == other.loading;

  @override
  int get hashCode => latLng.hashCode ^ loading.hashCode;
}
