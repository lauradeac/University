import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PlaylistComponent} from "./component/playlist/playlist.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'my-playlist',
    pathMatch: 'full'
  },
  {
    path: 'my-playlist',
    component: PlaylistComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
