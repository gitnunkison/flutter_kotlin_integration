import 'package:hooks_riverpod/hooks_riverpod.dart';

import '../../base/base_state.dart';
import 'home_controller.dart';


final homeControllerProvider = StateNotifierProvider<HomeController, BaseState>(
  (ref) {
    return HomeController();
  },
);
