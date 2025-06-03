import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from "./components/main-page/main-page.component";
import {AddEventComponent} from "./add-event/add-event.component";
import {EditEventComponent} from "./edit-event/edit-event.component";

const routes: Routes = [
  {
    path: 'main',
    component: MainPageComponent,
  },
  {
    path: 'add-event',
    component: AddEventComponent
  },
  { path: 'edit-event/:id',
    component: EditEventComponent },
  {
    path: '',
    //component: MainPageComponent
    redirectTo: 'main',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    {
      initialNavigation: 'enabledBlocking'
    })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
