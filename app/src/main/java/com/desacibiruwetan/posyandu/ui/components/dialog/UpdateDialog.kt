package com.desacibiruwetan.posyandu.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.desacibiruwetan.posyandu.data.model.AppVersion
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted

@Composable
fun UpdateDialog(
    updateInfo: AppVersion,
    onDismiss: () -> Unit,
    onUpdateClick: () -> Unit
) {
    Dialog(onDismissRequest = { if (!updateInfo.forceUpdate) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SurfaceWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Update Tersedia",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Inter,
                    color = PrimaryGreen
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Versi ${updateInfo.versionName} sudah tersedia untuk diunduh.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                if (!updateInfo.releaseNotes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Apa yang baru:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = updateInfo.releaseNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!updateInfo.forceUpdate) {
                        TextButton(onClick = onDismiss) {
                            Text("Nanti Saja", color = TextMuted)
                        }
                    }
                    
                    Button(
                        onClick = onUpdateClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Update Sekarang", color = Color.White)
                    }
                }
            }
        }
    }
}
