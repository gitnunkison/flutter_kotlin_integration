import 'package:flutter_kotlin_interaction/base/platform_interactor/platform_interaction_provider.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

import '../../base/base_state.dart';
import 'home_controller.dart';
import 'home_controller_impl.dart';

final homeControllerProvider = StateNotifierProvider<HomeController, BaseState>(
  (ref) {
    return HomeControllerImpl(
      ref.read(
        platformInteractionProvider,
      ),
    );
  },
);
